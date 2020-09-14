package com.sunasterisk.thooi.ui.post.newpost

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.sunasterisk.thooi.data.model.JobCategory
import com.sunasterisk.thooi.data.model.UserAddress
import com.sunasterisk.thooi.data.repository.FirestoreRepository
import com.sunasterisk.thooi.util.format
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime

class NewPostViewModel(private val firestoreRepo: FirestoreRepository) : ViewModel() {
    val places = MutableLiveData<UserAddress>()
    val category = MutableLiveData<JobCategory>()
    val suggestPrice = MutableLiveData<String>()
    val description = MutableLiveData<String>()
    val workTime get() = _workTime.map { it.first }

    private val _imageUri = MutableLiveData<MutableList<String>>()
    val imageUri: LiveData<MutableList<String>> get() = _imageUri

    private val _workTime = MutableLiveData<Triple<String, LocalDate, LocalDateTime>>()

    fun onPostClick() {

    }

    fun addImage(path: String) {
        _imageUri.value = (_imageUri.value ?: mutableListOf()).apply { add(path) }
    }

    fun removeImage(position: Int) {
        _imageUri.value?.let {
            it.removeAt(position)
            _imageUri.value = it
        }
    }

    fun onDatePick(date: Triple<String, LocalDate, LocalDateTime>) {
        _workTime.value = date
    }

    fun onTimePick(time: LocalTime) {
        val dateTime = _workTime.value
        dateTime?.run {
            val localDateTime = second.atTime(time)
            val text = localDateTime.format()
            _workTime.value = Triple(text, second, localDateTime)
        }
    }
}
