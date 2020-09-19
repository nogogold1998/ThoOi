package com.sunasterisk.thooi.ui.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import com.sunasterisk.thooi.data.repository.UserRepository
import com.sunasterisk.thooi.util.check
import com.sunasterisk.thooi.util.getOneShotResult
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SettingsViewModel(
    private val userRepo: UserRepository,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseInstanceId: FirebaseInstanceId,
    private val googleClient: GoogleSignInClient
) : ViewModel() {

    val email = MutableLiveData<String>()
    val name = MutableLiveData<String>()
    val imageUrl = MutableLiveData<String>()

    init {
        viewModelScope.launch {
            userRepo.getCurrentUser().collect { result ->
                result.check({
                    email.value = it.email
                    name.value = it.fullName
                    imageUrl.value = it.imageUrl
                })
            }
        }
    }

    fun signOutClick() {
        viewModelScope.launch {
            getOneShotResult {
                val token = firebaseInstanceId.instanceId.await().token
                userRepo.deleteToken(token)
            }
            googleClient.signOut().await()
            firebaseAuth.signOut()
        }
    }
}
