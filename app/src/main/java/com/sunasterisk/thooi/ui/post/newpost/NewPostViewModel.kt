package com.sunasterisk.thooi.ui.post.newpost

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.sunasterisk.thooi.data.model.JobCategory
import com.sunasterisk.thooi.data.model.UserAddress
import com.sunasterisk.thooi.data.repository.FirestoreRepository
import com.sunasterisk.thooi.util.format
import org.threeten.bp.LocalDateTime

class NewPostViewModel(private val firestoreRepo: FirestoreRepository) : ViewModel() {
    val places = MutableLiveData<UserAddress>()
    val category = MutableLiveData<JobCategory>()
    val suggestPrice = MutableLiveData<String>()
    val description = MutableLiveData<String>()
    val workTime get() = _workTime.map { it.format() }
    val imageUri = MutableLiveData<List<String>>()

    private val _workTime = MutableLiveData<LocalDateTime>()

    fun onPostClick() {

    }
}
