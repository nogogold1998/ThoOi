package com.sunasterisk.thooi.data.source

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.sunasterisk.thooi.data.Result
import com.sunasterisk.thooi.data.source.entity.User
import kotlinx.coroutines.flow.Flow

interface UserDataSource {
    interface Local {
        suspend fun saveUser(vararg user: User)

        fun getCurrentUser(): Flow<Result<User>>

        fun getUserBlocking(id: String): User?

        suspend fun getUser(id: String): User?

        fun getUserFlow(id: String): Flow<User>

        fun getAllUser(): Flow<List<User>>
    }

    interface Remote {
        fun getCurrentUser(): Flow<Result<User>>

        fun getUser(id: String): Flow<Result<User>>

        fun getAllUsers(): Flow<Result<List<User>>>

        suspend fun setToken(token: String): Result<Unit>

        suspend fun deleteToken(token: String): Result<Unit>

        suspend fun updateUser(user: User): Result<Unit>

        suspend fun resetPassword(email: String): Result<Unit>

        suspend fun signIn(username: String, password: String): Result<AuthResult>

        suspend fun signUp(user: User, password: String): Result<Unit>

        suspend fun signInWithCredential(user: User, credential: AuthCredential): Result<Unit>

        suspend fun checkGoogleAccount(credential: AuthCredential): Result<Boolean>

        fun signOut()
    }
}
