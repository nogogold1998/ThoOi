package com.sunasterisk.thooi.data.source.remote

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.internal.api.FirebaseNoSignedInUserException
import com.sunasterisk.thooi.data.Result
import com.sunasterisk.thooi.data.source.UserDataSource
import com.sunasterisk.thooi.data.source.entity.User
import com.sunasterisk.thooi.data.source.remote.RemoteConstants.EMAIL_NOT_VERIFIED
import com.sunasterisk.thooi.data.source.remote.RemoteConstants.FIELD_TOKEN
import com.sunasterisk.thooi.data.source.remote.RemoteConstants.MSG_EMAIL_NOT_VERIFIED
import com.sunasterisk.thooi.data.source.remote.RemoteConstants.MSG_USER_NOT_FOUND
import com.sunasterisk.thooi.data.source.remote.RemoteConstants.USERS_COLLECTION
import com.sunasterisk.thooi.data.source.remote.dto.FirestoreUser
import com.sunasterisk.thooi.util.getOneShotResult
import com.sunasterisk.thooi.util.getSnapshotFlow
import com.sunasterisk.thooi.util.toObjectWithId
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

@ExperimentalCoroutinesApi
class UserRemoteDataSource(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
) : UserDataSource.Remote {

    private val authException by lazy {
        FirebaseNoSignedInUserException(MSG_USER_NOT_FOUND)
    }

    private val userDocument
        get() = firebaseAuth.currentUser?.run {
            firestore.collection(USERS_COLLECTION).document(uid)
        }

    private val userCollection by lazy {
        firestore.collection(USERS_COLLECTION)
    }

    override fun getAllUsers(): Flow<Result<List<User>>> = callbackFlow {
        offer(Result.loading())
        val listener = userCollection.addSnapshotListener { snapshot, error ->
            try {
                snapshot?.documents
                    ?.mapNotNull { it.toObjectWithId(FirestoreUser::class.java, User::class) }
                    ?.let { offer(Result.success(it)) }
                    ?: throw error!!
            } catch (e: Exception) {
                offer(Result.failed(e))
            }
        }

        awaitClose {
            listener.remove()
            cancel()
        }
    }

    override suspend fun setToken(token: String) = getOneShotResult {
        userDocument?.update(FIELD_TOKEN, FieldValue.arrayUnion(token))?.await()
            ?: throw authException
        Unit
    }

    override suspend fun deleteToken(token: String) = getOneShotResult {
        userDocument?.update(FIELD_TOKEN, FieldValue.arrayRemove(token))?.await()
            ?: throw authException
        Unit
    }

    override suspend fun updateUser(user: User) = getOneShotResult {
        userDocument?.set(FirestoreUser(user))?.await() ?: throw authException
        Unit
    }

    override suspend fun resetPassword(email: String) = getOneShotResult {
        firebaseAuth.sendPasswordResetEmail(email).await()
        Unit
    }

    override fun getCurrentUser() = flow {
        userDocument?.run {
            emitAll(getSnapshotFlow { it.toObjectWithId(FirestoreUser::class.java, User::class) })
        } ?: emit(Result.failed(authException))
    }

    override suspend fun signIn(username: String, password: String) = getOneShotResult {
        firebaseAuth.signInWithEmailAndPassword(username, password).await().run {
            if (user?.isEmailVerified == false) {
                throw FirebaseAuthEmailException(EMAIL_NOT_VERIFIED, MSG_EMAIL_NOT_VERIFIED)
            } else {
                this
            }
        }
    }

    override suspend fun signUp(user: User, password: String) = getOneShotResult {
        firebaseAuth.createUserWithEmailAndPassword(user.email, password).await().user?.run {
            sendEmailVerification()
            firestore.collection(USERS_COLLECTION).document(uid).set(FirestoreUser(user)).await()
        } ?: throw authException
        Unit
    }

    override suspend fun signInWithCredential(user: User, credential: AuthCredential) =
        getOneShotResult {
            firebaseAuth.signInWithCredential(credential).await()
            userDocument?.run {
                if (get().await().exists().not()) set(FirestoreUser(user)).await()
                Unit
            } ?: throw authException
        }

    override suspend fun checkGoogleAccount(credential: AuthCredential) =
        getOneShotResult {
            firebaseAuth.signInWithCredential(credential).await()
            userDocument?.get()?.await()?.exists() ?: throw authException
        }

    override fun signOut() = firebaseAuth.signOut()
}
