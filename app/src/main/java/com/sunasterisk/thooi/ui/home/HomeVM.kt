package com.sunasterisk.thooi.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import com.google.firebase.auth.FirebaseAuth
import com.sunasterisk.thooi.R
import com.sunasterisk.thooi.data.Result
import com.sunasterisk.thooi.data.model.SummaryPost
import com.sunasterisk.thooi.data.repository.CategoryRepository
import com.sunasterisk.thooi.data.repository.PostRepository
import com.sunasterisk.thooi.data.source.entity.Category
import com.sunasterisk.thooi.data.source.entity.Post
import com.sunasterisk.thooi.data.source.entity.PostStatus
import com.sunasterisk.thooi.ui.home.model.CategoryAdapterItem
import com.sunasterisk.thooi.ui.home.model.HomeNavigationEvent
import com.sunasterisk.thooi.ui.home.model.PostCategoryItem
import com.sunasterisk.thooi.ui.home.model.TitleTextDividerItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.withContext

class HomeVM(
    categoryRepo: CategoryRepository,
    private val postRepo: PostRepository,
    private val firebaseAuth: FirebaseAuth,
) : ViewModel() {

    private val categoriesFlow = categoryRepo.getAllCategories()

    val categoryAdapterItems: LiveData<List<CategoryAdapterItem<*>>>
        get() = categoriesFlow.transformLatest {
            when (it) {
                is Result.Success -> emit(generateCategoryAdapterItems(it.data))
                is Result.Failed -> {
                }
                Result.Loading -> {
                }
            }
        }.asLiveData()

    private val _requestedUserId = MutableLiveData<String>()

    val summaryPostAdapterItems: LiveData<List<SummaryPost>>
        get() = _requestedUserId.asFlow()
            .flatMapConcat { postRepo.getPostsByUserId(it) }
            .transformLatest {
                when (it) {
                    is Result.Success -> emit(this@HomeVM.generateSummaryPosts(it.data))
                    is Result.Failed -> {
                    }
                    Result.Loading -> {
                    }
                }
            }
            .distinctUntilChanged()
            .asLiveData()

    private val _navigationEvent = MutableLiveData<HomeNavigationEvent<*>>()
    val navigationEvent: LiveData<HomeNavigationEvent<*>> get() = _navigationEvent

    fun loadDataByUserId(id: String) {
        _requestedUserId.postValue(id)
    }

    fun navigateToCategory(categoryId: String) {
    }

    fun navigateToPost(postId: String) {
        _navigationEvent.postValue(HomeNavigationEvent.ToPostDetailEvent(postId))
    }

    private suspend fun generateCategoryAdapterItems(
        categories: List<Category>,
    ): List<CategoryAdapterItem<*>> = withContext(Dispatchers.Default) {
        listOf(
            TitleTextDividerItem(R.string.label_discovery),
            *categories.map(::PostCategoryItem).toTypedArray(),
            TitleTextDividerItem(R.string.label_post)
        )
    }

    private suspend fun generateSummaryPosts(posts: List<Post>): List<SummaryPost> =
        withContext(Dispatchers.Default) {
            posts.filter {
                firebaseAuth.currentUser?.uid in setOf(
                    it.fixerId ?: "null",
                    it.customerRef,
                    *it.appliedFixerIds.toTypedArray()
                ) && it.status != PostStatus.FINISHED
            }
                .sortedByDescending { it.createdDateTime }
                .map(::SummaryPost)
        }
}
