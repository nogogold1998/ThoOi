package com.sunasterisk.thooi.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sunasterisk.thooi.R
import com.sunasterisk.thooi.data.model.SummaryPost
import com.sunasterisk.thooi.data.repository.CategoryRepository
import com.sunasterisk.thooi.data.repository.PostRepository
import com.sunasterisk.thooi.data.source.entity.Category
import com.sunasterisk.thooi.data.source.entity.Post
import com.sunasterisk.thooi.ui.home.model.CategoryAdapterItem
import com.sunasterisk.thooi.ui.home.model.HomeNavigationEvent
import com.sunasterisk.thooi.ui.home.model.PostCategoryItem
import com.sunasterisk.thooi.ui.home.model.TitleTextDividerItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeVM(
    private val categoryRepo: CategoryRepository,
    private val postRepo: PostRepository,
) : ViewModel() {

    private val _categoryAdapterItems = MutableLiveData<List<CategoryAdapterItem<*>>>()
    val categoryAdapterItems: LiveData<List<CategoryAdapterItem<*>>> get() = _categoryAdapterItems

    private val _summaryPostAdapterItems = MutableLiveData<List<SummaryPost>>()
    val summaryPostAdapterItems: LiveData<List<SummaryPost>> get() = _summaryPostAdapterItems

    private val _navigationEvent = MutableLiveData<HomeNavigationEvent<*>>()
    val navigationEvent: LiveData<HomeNavigationEvent<*>> get() = _navigationEvent

    fun loadDataByUserId(id: String) {
        viewModelScope.launch {
            categoryRepo.getAllCategories()
                .map(this@HomeVM::generateCategoryAdapterItems)
                .conflate()
                .collect {
                    _categoryAdapterItems.postValue(it)
                }

            postRepo.getPostsByUserId(id)
                .map(this@HomeVM::generateSummaryPosts)
                .conflate()
                .collect {
                    _summaryPostAdapterItems.postValue(it)
                }
        }
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
        withContext(Dispatchers.Default) { posts.map(::SummaryPost) }
}
