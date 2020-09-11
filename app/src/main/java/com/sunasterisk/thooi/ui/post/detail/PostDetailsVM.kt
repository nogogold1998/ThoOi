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
import androidx.lifecycle.viewModelScope
import androidx.savedstate.SavedStateRegistryOwner
import com.sunasterisk.thooi.R
import com.sunasterisk.thooi.data.model.PostDetail
import com.sunasterisk.thooi.data.model.SummaryUser
import com.sunasterisk.thooi.data.repository.PostDetailRepository
import com.sunasterisk.thooi.data.source.entity.PostStatus
import com.sunasterisk.thooi.data.source.entity.PostStatus.*
import com.sunasterisk.thooi.ui.post.detail.model.toPostDetailsAdapterItem
import com.sunasterisk.thooi.util.Event
import com.sunasterisk.thooi.util.postValue
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

/**
 * Created by Cong Vu Chi on 04/09/20 09:59.
 */
class PostDetailsVM(
    private val postDetailRepo: PostDetailRepository,
) : ViewModel() {

    private val _requestedPostId = MutableLiveData<String>()

    private val _selectedFixerId = MutableLiveData("")

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        _toastStringRes.postValue(R.string.error_unknown)
    }

    val postDetail = _requestedPostId.asFlow()
        .flatMapLatest { postDetailRepo.getPostDetailById(it) }
        .catch { _toastStringRes.postValue(R.string.error_unknown) }
        .asLiveData()

    val postDetailsAdapterItems = postDetail.asFlow()
        .combine(_selectedFixerId.asFlow()) { postDetail: PostDetail, selectedFixerId: String ->
            postDetail.toPostDetailsAdapterItem(selectedFixerId)
        }
        .conflate()
        .asLiveData()

    private val _toastStringRes = MutableLiveData<Event<@StringRes Int>>()
    val toastStringRes: LiveData<Event<Int>> get() = _toastStringRes

    fun loadPost(postId: String) {
        _requestedPostId.postValue(postId)
    }

    fun selectFixer(fixer: SummaryUser) {
        doWhenPostInLegalStatus(OPEN, errStringRes = R.string.error_unable_select_fixer) {
            _selectedFixerId.postValue(fixer.id)
        }
    }

    fun assignFixer() {
        doWhenPostInLegalStatus(OPEN) {
            val selectedFixerId = _selectedFixerId.value
            if (selectedFixerId.isNullOrBlank()) {
                _toastStringRes.postValue(R.string.error_not_select_fixer)
            } else {
                viewModelScope.launch(exceptionHandler) {
                    postDetailRepo.assignPostToFixer(it.id, selectedFixerId)
                }
                    .invokeOnCompletion { throwable ->
                        if (throwable == null) {
                            _selectedFixerId.value = ""
                            _toastStringRes.postValue(R.string.msg_assign_fixer_done)
                        }
                    }
            }
        }
    }

    fun reassignFixer() {
        doWhenPostInLegalStatus(PENDING) {
            viewModelScope.launch(exceptionHandler) {
                postDetailRepo.clearAssignedFixer(it.id)
            }
                .invokeOnCompletion {
                    if (it == null) _toastStringRes.postValue(R.string.msg_reassign_fixer_done)
                }
        }
    }

    fun cancelFixing() {
        doWhenPostInLegalStatus(ON_PROGRESS) {
            viewModelScope.launch(exceptionHandler) {
                postDetailRepo.cancelFixing(it.id)
            }
                .invokeOnCompletion {
                    if (it == null) _toastStringRes.postValue(R.string.msg_cancel_fixing_done)
                }
        }
    }

    fun finishFixing() {
        doWhenPostInLegalStatus(ON_PROGRESS) {
            viewModelScope.launch(exceptionHandler) {
                postDetailRepo.finishFixing(it.id)
            }
                .invokeOnCompletion {
                    if (it == null) _toastStringRes.postValue(R.string.label_job_finished)
                }
        }
    }

    fun closePost() {
        doWhenPostInLegalStatus(NEW, OPEN) {
            viewModelScope.launch(exceptionHandler) {
                postDetailRepo.closePost(it.id)
            }
                .invokeOnCompletion {
                    if (it == null) {
                        _toastStringRes.postValue(R.string.msg_closed_post_done)
                        _selectedFixerId.value = ""
                    }
                }
        }
    }

    private fun doWhenPostInLegalStatus(
        vararg status: PostStatus,
        errStringRes: Int = R.string.error_wrong_post_status,
        action: (PostDetail) -> Unit,
    ) {
        val postDetail = checkNotNull(postDetail.value)
        if (postDetail.status !in status) {
            _toastStringRes.postValue(errStringRes)
            return
        }
        action(postDetail)
    }

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
