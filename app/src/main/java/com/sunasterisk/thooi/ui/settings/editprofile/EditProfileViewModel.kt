package com.sunasterisk.thooi.ui.settings.editprofile

import android.annotation.SuppressLint
import androidx.lifecycle.*
import com.sunasterisk.thooi.R
import com.sunasterisk.thooi.data.model.UserAddress
import com.sunasterisk.thooi.data.repository.CategoryRepository
import com.sunasterisk.thooi.data.repository.UserRepository
import com.sunasterisk.thooi.data.source.entity.Category
import com.sunasterisk.thooi.data.source.entity.User
import com.sunasterisk.thooi.data.source.entity.UserType.FIXER
import com.sunasterisk.thooi.util.Event
import com.sunasterisk.thooi.util.check
import com.sunasterisk.thooi.util.isEmail
import com.sunasterisk.thooi.util.isValidPassword
import com.sunasterisk.thooi.util.livedata.NetworkState
import com.sunasterisk.thooi.util.livedata.NetworkStateLiveData
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter.ISO_LOCAL_DATE

@InternalCoroutinesApi
class EditProfileViewModel(
    private val userRepo: UserRepository,
    private val categoryRepo: CategoryRepository,
    network: NetworkStateLiveData
) : ViewModel() {

    private var user: User? = null

    init {
        viewModelScope.launch {
            userRepo.getCurrentUser().collect { result ->
                result.check({
                    user = it
                    fixer.value = it.userType == FIXER
                    name.value = it.fullName
                    email.value = it.email
                    phone.value = it.phone
                    bio.value = it.bio
                    birthday.value = it.dateOfBirth.format(ISO_LOCAL_DATE) to it.dateOfBirth
                    address.value = UserAddress("", "", it.address, it.location)
                })
            }
        }
    }

    val isNetworkConnected = network.map { it != NetworkState.NO_NETWORK }
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

    private var imageUrl: String? = null

    private val _save = MutableLiveData<Event<Unit>>()
    val save: LiveData<Event<Unit>> get() = _save

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

    fun save() {
        viewModelScope.launch {
            progress.value = true
            val user = getUser()
            if (user != null) {
                userRepo.updateUser(user).check({
                    _save.value = Event(Unit)
                }, {
                    nameRule.value = R.string.msg_unknown_error
                })
            }
            progress.value = false
        }
    }

    private fun getUser(): User? {
        var result: User? = user
        val nameVal = name.value
        val emailVal = email.value
        val phoneVal = phone.value

        val isValid = validate(nameVal?.isNotBlank() == true, nameRule)
                && validate(phoneVal?.isNotBlank() == true, phoneRule)
                && validate(emailVal?.isNotBlank() == true, emailRule)
                && validate(emailVal?.isEmail == true, emailRule, R.string.msg_email_invalid)

        if (isValid && emailVal != null && phoneVal != null && nameVal != null) {
            result = result?.also {
                it.fullName = nameVal
                it.phone = phoneVal
                bio.value?.run { it.bio = this }
                birthday.value?.second?.run { it.dateOfBirth = this }
            }
        }

        return result
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

}
