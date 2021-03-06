package com.sunasterisk.thooi.ui.signin

import androidx.lifecycle.*
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.iid.FirebaseInstanceId
import com.sunasterisk.thooi.R
import com.sunasterisk.thooi.data.repository.UserRepository
import com.sunasterisk.thooi.util.*
import com.sunasterisk.thooi.util.livedata.NetworkState
import com.sunasterisk.thooi.util.livedata.NetworkStateLiveData
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SignInViewModel(
    private val userRepo: UserRepository,
    private val firebaseInstanceId: FirebaseInstanceId,
    network: NetworkStateLiveData,
    private val googleClient: GoogleSignInClient,
    private val firebaseAuth: FirebaseAuth,
) : ViewModel() {

    init {
        viewModelScope.launch {
            if (firebaseAuth.currentUser == null) {
                googleClient.signOut()
            }
            userRepo.getAllUsers().filterNotNull().first { it.isNotEmpty() }
        }
    }

    val isNetworkConnected = network.map { it != NetworkState.NO_NETWORK }
    val email = MutableLiveData<String>()
    val emailRule = email.transform { if (it.isEmail) null else R.string.msg_email_invalid }

    val password = MutableLiveData<String>()
    val passwordRule =
        password.transform { if (it.isValidPassword) null else R.string.msg_weak_password }

    private val _signIn = MutableLiveData<Event<Unit>>()
    val signIn: LiveData<Event<Unit>> get() = _signIn

    fun signInClick() {
        viewModelScope.launch {
            getValidValue()?.apply {
                userRepo.signIn(first, second).check({
                    _signIn.value = Event(Unit)
                    viewModelScope.launch {
                        getOneShotResult {
                            val token = firebaseInstanceId.instanceId.await().token
                            userRepo.setToken(token)
                        }
                    }
                }, {
                    when (it) {
                        is FirebaseAuthInvalidUserException -> {
                            emailRule.value = R.string.msg_email_not_exist
                        }
                        is FirebaseAuthInvalidCredentialsException -> {
                            passwordRule.value = R.string.msg_password_invalid
                        }
                        is FirebaseAuthEmailException -> {
                            passwordRule.value = R.string.msg_email_not_verified
                        }
                        else -> passwordRule.value = R.string.msg_unknown_error
                    }
                })
            }
        }
    }

    fun forgotPasswordClick(emailAddress: String) {
        viewModelScope.launch {
            userRepo.resetPassword(emailAddress)
        }
    }

    private fun getValidValue(): Pair<String, String>? {
        val emailValue = email.value
        val passwordValue = password.value
        when {
            emailValue.isNullOrBlank() -> emailRule.value = R.string.msg_require_not_empty
            passwordValue.isNullOrBlank() -> passwordRule.value = R.string.msg_require_not_empty
            emailRule.value == null || passwordRule.value == null -> return emailValue to passwordValue
        }
        return null
    }
}
