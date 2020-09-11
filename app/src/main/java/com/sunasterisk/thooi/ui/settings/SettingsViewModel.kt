package com.sunasterisk.thooi.ui.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import com.sunasterisk.thooi.data.model.UserAddress
import com.sunasterisk.thooi.data.repository.UserRepository
import com.sunasterisk.thooi.util.check
import com.sunasterisk.thooi.util.format
import com.sunasterisk.thooi.util.getOneShotResult
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.threeten.bp.LocalDate

class SettingsViewModel(
    private val userRepo: UserRepository,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseInstanceId: FirebaseInstanceId
) : ViewModel() {

    val email = MutableLiveData<String>()
    val name = MutableLiveData<String>()
    val imageUrl = MutableLiveData<String>()

    val fixer = MutableLiveData<Boolean>()
    val phone = MutableLiveData<String>()
    val birthday = MutableLiveData<Pair<String, LocalDate>>()
    val bio = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val address = MutableLiveData<UserAddress?>()

    val nameRule = MutableLiveData<Int?>()
    val emailRule = MutableLiveData<Int?>()
    val phoneRule = MutableLiveData<Int?>()
    val passwordRule = MutableLiveData<Int?>()
    val progress = MutableLiveData<Boolean>()

    fun save() {

    }

    init {
        viewModelScope.launch {
            userRepo.getCurrentUser().collect { result ->
                result.check({
                    email.value = it.email
                    name.value = it.fullName
                    imageUrl.value = it.imageUrl
                    phone.value = it.phone
                    birthday.value = it.dateOfBirth.format() to it.dateOfBirth
                    bio.value = it.bio
                    address.value = UserAddress(it.id, "", it.address, it.location)
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
            firebaseAuth.signOut()
        }
    }
}
