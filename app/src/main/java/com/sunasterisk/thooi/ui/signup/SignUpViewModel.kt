package com.sunasterisk.thooi.ui.signup

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.GoogleAuthProvider
import com.sunasterisk.thooi.R
import com.sunasterisk.thooi.data.model.UserAddress
import com.sunasterisk.thooi.data.repository.CategoryRepository
import com.sunasterisk.thooi.data.repository.UserRepository
import com.sunasterisk.thooi.data.source.entity.Category
import com.sunasterisk.thooi.data.source.entity.User
import com.sunasterisk.thooi.data.source.entity.UserType.CUSTOMER
import com.sunasterisk.thooi.data.source.entity.UserType.FIXER
import com.sunasterisk.thooi.util.Event
import com.sunasterisk.thooi.util.check
import com.sunasterisk.thooi.util.isEmail
import com.sunasterisk.thooi.util.isValidPassword
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate

@InternalCoroutinesApi
class SignUpViewModel(
    private val userRepo: UserRepository,
    private val categoryRepo: CategoryRepository
) : ViewModel() {

    val isGoogle = MutableLiveData(false)
    val fixer = MutableLiveData(false)
    val name = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val phone = MutableLiveData<String>()
    val birthday = MutableLiveData<Pair<String, LocalDate>>()
    val bio = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val address = MutableLiveData<UserAddress?>()

    val nameRule = MutableLiveData<Int?>()
    val emailRule = MutableLiveData<Int?>()
    val phoneRule = MutableLiveData<Int?>()
    val passwordRule = MutableLiveData<Int?>()
    val progress = MutableLiveData(false)

    private var credential: AuthCredential? = null
    private var imageUrl: String? = null

    private val _signUp = MutableLiveData<Event<Unit>>()
    val signUp: LiveData<Event<Unit>> get() = _signUp

    private val _googleSignIn = MutableLiveData<Event<Unit>>()
    val googleSignIn: LiveData<Event<Unit>> get() = _googleSignIn

    private val _category = MutableLiveData<List<Category>>()
    val category: LiveData<List<Category>> get() = _category

    private val _profession = MutableLiveData<List<String>>()
    val profession: LiveData<List<String>> get() = _profession

    init {
        viewModelScope.launch {
            categoryRepo.getAllCategories().collect { result ->
                result.check({
                    _category.value = it
                }, {
                    nameRule.value = R.string.msg_unknown_error
                })
            }
        }
    }

    fun signUp() {
        viewModelScope.launch {
            progress.value = true
            val user = getUser()
            val password = getPassword()
            val authCredential = credential
            if (isGoogle.value == true && authCredential != null && user != null) {
                userRepo.signInWithCredential(user, authCredential).check({
                    _googleSignIn.value = Event(Unit)
                }, {
                    nameRule.value = R.string.msg_unknown_error
                })
            } else if (user != null && password != null) {
                userRepo.signUp(user, password).check({
                    _signUp.value = Event(Unit)
                }, {
                    if (it is FirebaseAuthUserCollisionException && it.errorCode == ERROR_EMAIL_ALREADY_IN_USE) {
                        nameRule.value = R.string.msg_email_already_in_use
                    } else {
                        nameRule.value = R.string.msg_unknown_error
                    }
                })
            }
            progress.value = false
        }
    }

    fun signInWithGoogleClick(account: GoogleSignInAccount) {
        account.let {
            isGoogle.value = true
            name.value = it.displayName
            email.value = it.email
            imageUrl = it.photoUrl?.path
            credential = it.idToken?.run { GoogleAuthProvider.getCredential(this, null) }
            viewModelScope.launch {
                credential?.let { value ->
                    userRepo.checkGoogleAccount(value).check({ done ->
                        if (done) _googleSignIn.value = Event(Unit)
                    })
                }
            }
        }
    }

    private fun getUser(): User? {
        var result: User? = null
        val nameVal = name.value
        val emailVal = email.value
        val phoneVal = phone.value

        val isValid = validate(nameVal?.isNotBlank() == true, nameRule)
                && validate(phoneVal?.isNotBlank() == true, phoneRule)
                && validate(emailVal?.isNotBlank() == true, emailRule)
                && validate(emailVal?.isEmail == true, emailRule, R.string.msg_email_invalid)

        if (isValid && emailVal != null && phoneVal != null && nameVal != null) {
            result = User(
                email = emailVal,
                fullName = nameVal,
                phone = phoneVal,
                imageUrl = imageUrl ?: "https://firebasestorage.googleapis.com/v0/b/tho-oi.appspot.com/o/avatar%2Favatar.png?alt=media&token=bc002db7-3c76-4509-b7c8-f41f9372ccc3",
                dateOfBirth = birthday.value?.second ?: LocalDate.now(),
                address = address.value?.address ?: "",
                professions = profession.value ?: emptyList(),
                bio = bio.value ?: "",
                location = address.value?.location ?: LatLng(0.0, 0.0),
                userType = if (fixer.value == true) FIXER else CUSTOMER
            )
        }

        return result
    }

    fun pickCategory(string: String) {
        _profession.value = listOf(string)
    }

    @SuppressLint("NullSafeMutableLiveData")
    private fun getPassword() = password.value?.run {
        if (isNotBlank() && isValidPassword) {
            passwordRule.value = null
            this
        } else {
            passwordRule.value = R.string.msg_weak_password
            null
        }
    }

    @SuppressLint("NullSafeMutableLiveData")
    private fun validate(isValid: Boolean, rule: MutableLiveData<Int?>, msg: Int? = null) =
        when {
            !isValid && msg != null -> {
                rule.value = msg
                false
            }
            !isValid && msg == null -> {
                rule.value = R.string.msg_require_not_empty
                false
            }
            else -> {
                rule.value = null
                true
            }
        }

    companion object {
        const val ERROR_EMAIL_ALREADY_IN_USE = "ERROR_EMAIL_ALREADY_IN_USE"
    }
}
