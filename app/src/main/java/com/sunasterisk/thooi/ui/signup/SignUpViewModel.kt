package com.sunasterisk.thooi.ui.signup

import android.Manifest
import android.annotation.SuppressLint
import androidx.annotation.RequiresPermission
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.GoogleAuthProvider
import com.sunasterisk.thooi.R
import com.sunasterisk.thooi.data.model.UserAddress
import com.sunasterisk.thooi.data.repository.UserRepository
import com.sunasterisk.thooi.data.source.entity.User
import com.sunasterisk.thooi.data.source.entity.UserType.CUSTOMER
import com.sunasterisk.thooi.data.source.entity.UserType.FIXER
import com.sunasterisk.thooi.util.*
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate

class SignUpViewModel(
    private val userRepo: UserRepository,
    private val placesClient: PlacesClient
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

    private val _places = MutableLiveData<List<UserAddress>>()
    val places: LiveData<List<UserAddress>> get() = _places

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
                    userRepo.checkGoogleAccount(value).check {
                        _googleSignIn.value = Event(Unit)
                    }
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
                imageUrl = imageUrl ?: "",
                dateOfBirth = birthday.value?.second ?: LocalDate.now(),
                address = address.value?.address ?: "",
                location = address.value?.location ?: LatLng(0.0, 0.0),
                userType = if (fixer.value == true) FIXER else CUSTOMER
            )
        }

        return result
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_WIFI_STATE])
    fun findAddress() {
        viewModelScope.launch {
            placesClient.getCurrentPlaces().run {
                _places.value = map { it.place.run { UserAddress(id, name, address, latLng) } }
            }
        }
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
