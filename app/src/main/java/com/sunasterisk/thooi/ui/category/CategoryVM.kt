package com.sunasterisk.thooi.ui.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import com.sunasterisk.thooi.data.Result
import com.sunasterisk.thooi.data.model.SummaryUser
import com.sunasterisk.thooi.data.repository.CategoryRepository
import com.sunasterisk.thooi.data.repository.PostRepository
import com.sunasterisk.thooi.data.repository.UserRepository
import com.sunasterisk.thooi.data.source.entity.Category
import com.sunasterisk.thooi.data.source.entity.User
import com.sunasterisk.thooi.data.source.entity.UserType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.withContext

class CategoryVM(
    private val categoryRepository: CategoryRepository,
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
) : ViewModel() {

    private val a = userRepository.getAllUsers()
    private val b = postRepository.getAllPosts()

    private val _d = MutableLiveData<String>()
    private val e = userRepository.getCurrentUser().filterIsInstance<Result.Success<User>>()

    private val c = combine(a, b, _d.asFlow(), e) { a1, b1, d1, e1 ->
        withContext(Dispatchers.Main) {
            val a2 = async {
                a1.filter { user ->
                    user.professions.any { it == d1 } && user.userType == UserType.FIXER
                }
                    .map { CategoryAdapterItem.FixerItem(SummaryUser(it)) }
            }
            val b2 = async {
                b1.filter { it.categoryRef == d1 }.map { CategoryAdapterItem.PostItem(it) }
            }
            if (e1.data.userType == UserType.CUSTOMER) a2.await() else b2.await()
        }
    }
    val adapterItems: LiveData<List<CategoryAdapterItem<*>>> = c.asLiveData()

    val category: LiveData<Category> = _d.asFlow()
        .flatMapLatest { categoryRepository.getCategoryByIdFlow(it) }
        .transform { if (it is Result.Success) emit(it.data) }
        .asLiveData()

    fun requestCategory(categoryId: String) {
        _d.postValue(categoryId)
    }
}
