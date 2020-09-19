package com.sunasterisk.thooi.ui.post.newpost

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sunasterisk.thooi.data.repository.CategoryRepository
import com.sunasterisk.thooi.data.source.entity.Category
import com.sunasterisk.thooi.util.check
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CategoryViewModel(private val categoryRepo: CategoryRepository) : ViewModel() {

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> get() = _categories

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> get() = _error

    val loading = MutableLiveData(true)

    init {
        viewModelScope.launch {
            categoryRepo.getAllCategories().collect { result ->
                result.check({
                    _categories.value = it
                }, {
                    _error.value = it
                }, {
                    loading.value = it
                })
            }
        }
    }
}
