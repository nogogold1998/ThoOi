package com.sunasterisk.thooi.data.repository

import com.google.firebase.auth.AuthCredential
import com.sunasterisk.thooi.data.Result
import com.sunasterisk.thooi.data.source.UserDataSource
import com.sunasterisk.thooi.data.source.entity.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch

class UserRepositoryImpl(
    private val remote: UserDataSource.Remote,
    private val local: UserDataSource.Local,
) : UserRepository {

    private val allUsersFlow by lazy {
        merge(
            remote.getAllUsers().transform {
                if (it is Result.Success) local.saveUser(*it.data.toTypedArray())
            },
            local.getAllUser()
        )
    }

    private val cachedUserFlows = hashMapOf<String, Flow<Result<User>>>()

    @ExperimentalCoroutinesApi
    override fun getCurrentUser(): Flow<Result<User>> = channelFlow {
        launch {
            remote.getCurrentUser().collect {
                if (it is Result.Success) local.saveUser(it.data) else offer(it)
            }
        }
        launch { local.getCurrentUser().collect { offer(it) } }
    }

    override fun getAllUsers(): Flow<List<User>> = allUsersFlow

    override suspend fun getUser(id: String): User? = local.getUser(id)

    override fun getUserFlow(id: String): Flow<Result<User>> = synchronized(cachedUserFlows) {
        cachedUserFlows.getOrPut(id) {
            channelFlow<Result<User>> {
                launch {
                    remote.getUser(id)
                        .filterIsInstance<Result.Success<User>>()
                        .distinctUntilChanged()
                        .collect { local.saveUser(it.data) }
                }
                launch {
                    local.getUserFlow(id)
                        .map { Result.success(it) }
                        .distinctUntilChanged()
                        .collect { offer(it) }
                }
            }.onStart { emit(Result.loading()) }
        }
    }

    override suspend fun getUser(id: String): User? = null

    override suspend fun updateUser(user: User) = remote.updateUser(user)

    override suspend fun resetPassword(email: String) = remote.resetPassword(email)

    override suspend fun setToken(token: String) = remote.setToken(token)

    override suspend fun signIn(username: String, password: String) =
        remote.signIn(username, password)

    override suspend fun signUp(user: User, password: String) = remote.signUp(user, password)

    override suspend fun signInWithCredential(user: User, credential: AuthCredential) =
        remote.signInWithCredential(user, credential)

    override suspend fun checkGoogleAccount(credential: AuthCredential) =
        remote.checkGoogleAccount(credential)

    override fun signOut() = remote.signOut()

}
