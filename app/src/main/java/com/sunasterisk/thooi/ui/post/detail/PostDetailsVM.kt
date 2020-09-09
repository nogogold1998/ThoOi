package com.sunasterisk.thooi.ui.post.detail

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.savedstate.SavedStateRegistryOwner
import com.sunasterisk.thooi.R
import com.sunasterisk.thooi.data.repository.PostDetailRepository
import com.sunasterisk.thooi.util.Event
import com.sunasterisk.thooi.util.postValue
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest

/**
 * Created by Cong Vu Chi on 04/09/20 09:59.
 */
class PostDetailsVM(
    private val postDetailRepo: PostDetailRepository,
) : ViewModel() {

    private val _requestedPostId = MutableLiveData<String>()

    val postDetail = _requestedPostId.asFlow()
        .flatMapLatest { postDetailRepo.getPostDetailById(it) }
        .catch { _errorRes.postValue(R.string.error_unknown) }
        .asLiveData()

    private val _errorRes = MutableLiveData<Event<@StringRes Int>>()
    val errorRes: LiveData<Event<Int>> get() = _errorRes

    fun loadPost(postId: String) = _requestedPostId.postValue(postId)

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val postDetailRepo: PostDetailRepository,
        owner: SavedStateRegistryOwner,
        defaultArgs: Bundle? = null,
    ) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {

        override fun <T : ViewModel> create(
            key: String,
            modelClass: Class<T>,
            handle: SavedStateHandle,
        ) = if (modelClass.isAssignableFrom(PostDetailsVM::class.java)) {
            PostDetailsVM(postDetailRepo)
        } else {
            throw IllegalStateException("Unknown ViewModel class: ${modelClass.name}")
        } as T
    }
}
