package com.sunasterisk.thooi.ui.post.detail

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.sunasterisk.thooi.R
import com.sunasterisk.thooi.data.model.PostDetail
import com.sunasterisk.thooi.data.model.SummaryUser
import com.sunasterisk.thooi.data.repository.PostDetailRepository
import com.sunasterisk.thooi.data.source.entity.PostStatus
import com.sunasterisk.thooi.data.source.entity.PostStatus.*
import com.sunasterisk.thooi.data.source.entity.UserType
import com.sunasterisk.thooi.ui.post.detail.model.PostDetailsAdapterItem
import com.sunasterisk.thooi.util.Event
import com.sunasterisk.thooi.util.postValue
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch

private val TAG = "PostDetailVM"
sealed class PostDetailsVM(
    postDetailRepo: PostDetailRepository,
    protected val userType: UserType,
) : ViewModel() {
    protected val _requestedPostId = MutableLiveData<String>()

    val postDetail: LiveData<PostDetail> = _requestedPostId.asFlow()
        .flatMapLatest { postDetailRepo.getPostDetailById(it) }
        .catch {
            Log.d(TAG, "PostDetailsVM.getPostDetail: .catch()")
            it.printStackTrace()
            _toastStringRes.postValue(R.string.error_unknown)
        }
        .asLiveData()

    abstract val postDetailsAdapterItems: LiveData<List<PostDetailsAdapterItem<out Any>>>

    protected val _toastStringRes = MutableLiveData<Event<@StringRes Int>>()
    val toastStringRes: LiveData<Event<Int>> get() = _toastStringRes

    protected suspend fun getPostDetailsAdapterItem(
        postDetail: PostDetail,
        selectedFixerId: String? = null,
    ) =
        coroutineScope {
            val top = async { PostDetailsAdapterItem.PostDetailsItem(postDetail) }
            val middle = async {
                postDetail.appliedFixers.map { summaryUser ->
                    val isSelected =
                        summaryUser.id in listOf(selectedFixerId, postDetail.assignedFixerId)
                    PostDetailsAdapterItem.SummaryUserItem(summaryUser, isSelected)
                }.toTypedArray()
            }
            val bottom = PostDetailsAdapterItem.ActionBottomItem(userType)
            listOf(top.await(), *middle.await(), bottom)
        }

    protected fun doWhenPostInLegalStatus(
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

    fun loadPost(postId: String) {
        _requestedPostId.postValue(postId)
    }
}

class CustomerPostDetailsVM(
    private val postDetailRepo: PostDetailRepository,
) : PostDetailsVM(postDetailRepo, UserType.CUSTOMER) {

    private val _selectedFixerId = MutableLiveData("")

    private val exceptionHandler = CoroutineExceptionHandler { _, th ->
        Log.d(TAG, "CustomerPostDetailsVM.exceptionHandler: ")
        th.printStackTrace()
        _toastStringRes.postValue(R.string.error_unknown)
    }

    override val postDetailsAdapterItems = postDetail.asFlow()
        .combine(_selectedFixerId.asFlow()) { postDetail: PostDetail, selectedFixerId: String ->
            getPostDetailsAdapterItem(postDetail, selectedFixerId)
        }
        .conflate()
        .asLiveData()

    fun selectFixer(fixer: SummaryUser) =
        doWhenPostInLegalStatus(OPEN, errStringRes = R.string.error_unable_select_fixer) {
            _selectedFixerId.postValue(fixer.id)
        }

    fun assignFixer() = doWhenPostInLegalStatus(OPEN) {
        val selectedFixerId = _selectedFixerId.value
        if (selectedFixerId.isNullOrBlank()) {
            _toastStringRes.postValue(R.string.error_not_select_fixer)
            return@doWhenPostInLegalStatus
        }
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

    fun reassignFixer() = doWhenPostInLegalStatus(PENDING) {
        viewModelScope.launch(exceptionHandler) {
            postDetailRepo.clearAssignedFixer(it.id)
        }
            .invokeOnCompletion {
                if (it == null) _toastStringRes.postValue(R.string.msg_reassign_fixer_done)
            }
    }

    fun cancelFixing() = doWhenPostInLegalStatus(ON_PROGRESS) {
        viewModelScope.launch(exceptionHandler) {
            postDetailRepo.cancelFixing(it.id)
        }
            .invokeOnCompletion {
                if (it == null) _toastStringRes.postValue(R.string.msg_cancel_fixing_done)
            }
    }

    fun finishFixing() = doWhenPostInLegalStatus(ON_PROGRESS) {
        viewModelScope.launch(exceptionHandler) {
            postDetailRepo.finishFixing(it.id)
        }
            .invokeOnCompletion {
                if (it == null) _toastStringRes.postValue(R.string.label_job_finished)
            }
    }

    fun closePost() = doWhenPostInLegalStatus(NEW, OPEN) {
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

class FixerPostDetailsVM(
    private val postDetailRepo: PostDetailRepository,
) : PostDetailsVM(postDetailRepo, UserType.FIXER) {

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        _toastStringRes.postValue(R.string.error_unknown)
    }

    override val postDetailsAdapterItems = postDetail.asFlow()
        .mapLatest { getPostDetailsAdapterItem(it) }
        .conflate()
        .asLiveData()

    fun applyJob() = doWhenPostInLegalStatus(NEW, OPEN) {
        viewModelScope.launch(exceptionHandler) {
            postDetailRepo.applyJob(it.id)
        }
    }

    fun startFixing() = doWhenPostInLegalStatus(PENDING) {
        viewModelScope.launch(exceptionHandler) {
            postDetailRepo.startFixing(it.id)
        }
    }
}
