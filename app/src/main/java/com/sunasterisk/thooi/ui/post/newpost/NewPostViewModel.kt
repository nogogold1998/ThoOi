package com.sunasterisk.thooi.ui.post.newpost

import android.net.Uri
import androidx.lifecycle.*
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.storage.FirebaseStorage
import com.sunasterisk.thooi.data.model.UserAddress
import com.sunasterisk.thooi.data.repository.FirestoreRepository
import com.sunasterisk.thooi.data.repository.UserRepository
import com.sunasterisk.thooi.data.source.entity.Category
import com.sunasterisk.thooi.data.source.entity.Post
import com.sunasterisk.thooi.util.Event
import com.sunasterisk.thooi.util.check
import com.sunasterisk.thooi.util.format
import com.sunasterisk.thooi.util.getOneShotResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import java.io.File

class NewPostViewModel(
    private val firestoreRepo: FirestoreRepository,
    private val firebaseAuth: FirebaseAuth,
    private val fireStorage: FirebaseStorage,
    private val firebaseInstanceId: FirebaseInstanceId,
    private val userRepo: UserRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            getOneShotResult {
                val token = firebaseInstanceId.instanceId.await().token
                userRepo.setToken(token)
            }
        }
    }

    val places = MutableLiveData<UserAddress>()
    val category = MutableLiveData<Category>()
    val suggestPrice = MutableLiveData<String>()
    val description = MutableLiveData<String>()
    val workTime get() = _workTime.map { it.first }
    val loading = MutableLiveData(false)

    private val _imageUri = MutableLiveData<MutableList<String>>()
    val imageUri: LiveData<MutableList<String>> get() = _imageUri

    private val _workTime = MutableLiveData<Triple<String, LocalDate, LocalDateTime>>()

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> get() = _error

    private val _done = MutableLiveData<Event<Unit>>()
    val done: LiveData<Event<Unit>> get() = _done

    fun onPostClick() {
        viewModelScope.launch {

            loading.value = true
            val imageUrls = withContext(Dispatchers.IO) {
                val results = mutableListOf<String>()
                imageUri.value?.forEach {
                    val file = File(it)
                    val fileUri = Uri.fromFile(file)
                    val ref = fireStorage.reference.child("post/${fileUri.lastPathSegment}")
                    try {
                        ref.putFile(fileUri).await()
                        val path = ref.downloadUrl.await()?.toString() ?: return@forEach
                        results.add(path)
                    } catch (e: Exception) {
                        _error.value = e
                    }
                    try {
                        file.deleteOnExit()
                    } catch (e: Exception) {
                        _error.value = e
                    }
                }

                results
            }

            val post = Post(
                address = places.value?.address ?: "",
                location = places.value?.location ?: LatLng(0.0, 0.0),
                categoryRef = category.value?.id ?: "",
                customerRef = firebaseAuth.uid ?: "",
                description = description.value ?: "",
                suggestedPrice = suggestPrice.value ?: "Thoả thuận",
                appointment = _workTime.value?.third ?: LocalDateTime.now(),
                imagesRefs = imageUrls
            )
            firestoreRepo.newPost(post).check({
                _done.value = Event(Unit)
            }, {
                _error.value = it
            }, {
                loading.value = it
            })
        }
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
