package com.sunasterisk.thooi.ui.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.sunasterisk.thooi.R
import com.sunasterisk.thooi.data.repository.UserRepository
import com.sunasterisk.thooi.util.*
import kotlinx.coroutines.launch

class SignInViewModel(private val userRepo: UserRepository) : ViewModel() {

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
