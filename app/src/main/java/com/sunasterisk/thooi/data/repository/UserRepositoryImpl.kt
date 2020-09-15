package com.sunasterisk.thooi.data.repository

import com.google.firebase.auth.AuthCredential
import com.sunasterisk.thooi.data.Result
import com.sunasterisk.thooi.data.source.UserDataSource
import com.sunasterisk.thooi.data.source.entity.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.transform

class UserRepositoryImpl(
    private val local: UserDataSource.Local,
    private val remote: UserDataSource.Remote,
) : UserRepository {

    @ExperimentalCoroutinesApi
    override fun getCurrentUser() = merge(
        remote.getCurrentUser().transform {
            if (it is Result.Success) local.setCurrentUser(it.data) else emit(it)
        },
        local.getCurrentUser()
    )

    override suspend fun getUser(id: String): User? = null

    override suspend fun updateUser(user: User) = remote.updateUser(user)

    override suspend fun resetPassword(email: String) = remote.resetPassword(email)

    override suspend fun setToken(token: String) = remote.setToken(token)

    override suspend fun signIn(username: String, password: String) =
        remote.signIn(username, password)

    override suspend fun signUp(user: User, password: String) = remote.signUp(user, password)

    override suspend fun signInWithCredential(user: User, credential: AuthCredential) =
        remote.signInWithCredential(user, credential)

    override fun signOut() = remote.signOut()

}
