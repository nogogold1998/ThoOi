package com.sunasterisk.thooi.data.repository

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.sunasterisk.thooi.data.Result
import com.sunasterisk.thooi.data.source.entity.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getCurrentUser(): Flow<Result<User>>

    suspend fun updateUser(user: User): Result<Unit>

    suspend fun resetPassword(email: String): Result<Unit>

    suspend fun setToken(token: String): Result<Unit>

    suspend fun signIn(username: String, password: String): Result<AuthResult>

    suspend fun signUp(user: User, password: String): Result<Unit>

    suspend fun signInWithCredential(user: User, credential: AuthCredential): Result<Unit>

    suspend fun checkGoogleAccount(credential: AuthCredential): Result<Boolean>

    fun signOut()
}
