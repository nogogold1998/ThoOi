package com.sunasterisk.thooi.data.source

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.sunasterisk.thooi.data.Result
import com.sunasterisk.thooi.data.source.entity.User
import kotlinx.coroutines.flow.Flow

interface UserDataSource {
    interface Local {
        suspend fun setCurrentUser(user: User)

        fun getCurrentUser(): Flow<Result<User>>

        fun getUserBlocking(id: String): User?
    }

    interface Remote {
        fun getCurrentUser(): Flow<Result<User>>

        suspend fun setToken(token: String): Result<Unit>

        suspend fun updateUser(user: User): Result<Unit>

        suspend fun resetPassword(email: String): Result<Unit>

        suspend fun signIn(username: String, password: String): Result<AuthResult>

        suspend fun signUp(user: User, password: String): Result<Unit>

        suspend fun signInWithCredential(user: User, credential: AuthCredential): Result<Unit>

        fun signOut()
    }
}
