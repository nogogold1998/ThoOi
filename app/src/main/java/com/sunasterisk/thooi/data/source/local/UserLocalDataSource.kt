package com.sunasterisk.thooi.data.source.local

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.sunasterisk.thooi.data.Result
import com.sunasterisk.thooi.data.source.UserDataSource
import com.sunasterisk.thooi.data.source.entity.User
import com.sunasterisk.thooi.data.source.local.database.dao.UserDao
import com.sunasterisk.thooi.data.source.remote.RemoteConstants.MSG_USER_NOT_FOUND
import com.sunasterisk.thooi.data.source.remote.RemoteConstants.OBJECT_NOT_FOUND
import kotlinx.coroutines.flow.*

class UserLocalDataSource(private val userDao: UserDao, private val firebaseAuth: FirebaseAuth) :
    UserDataSource.Local {
    override suspend fun setCurrentUser(user: User) = userDao.insert(user)

    override fun getCurrentUser() = flow {
        firebaseAuth.currentUser?.uid?.let {
            emitAll(userDao.findUserByIdFlow(it).filterNotNull().map { value ->
                Result.success(value)
            })
        } ?: emit(Result.failed(FirebaseAuthException(OBJECT_NOT_FOUND, MSG_USER_NOT_FOUND)))
    }

    override fun getUserBlocking(id: String): User? = userDao.findUserByIdBlocking(id)
}
