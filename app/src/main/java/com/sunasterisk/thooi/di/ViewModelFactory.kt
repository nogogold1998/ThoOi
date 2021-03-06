package com.sunasterisk.thooi.di

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import com.sunasterisk.thooi.data.Result
import com.sunasterisk.thooi.data.repository.UserRepository
import com.sunasterisk.thooi.data.source.entity.UserType
import com.sunasterisk.thooi.ui.category.CategoryVM
import com.sunasterisk.thooi.ui.home.HomeVM
import com.sunasterisk.thooi.ui.main.MainVM
import com.sunasterisk.thooi.ui.post.detail.CustomerPostDetailsVM
import com.sunasterisk.thooi.ui.post.detail.FixerPostDetailsVM
import com.sunasterisk.thooi.ui.post.detail.PostDetailsVM
import com.sunasterisk.thooi.ui.profile.ProfileVM
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.inject

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle? = null,
) : AbstractSavedStateViewModelFactory(owner, defaultArgs), KoinComponent {
    private val userRepo: UserRepository by inject()
    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle,
    ): T = with(modelClass) {
        when {
            isAssignableFrom(MainVM::class.java) -> get<MainVM>()
            isAssignableFrom(HomeVM::class.java) -> get<HomeVM>()
            isAssignableFrom(PostDetailsVM::class.java) -> runBlocking {
                val user1 =
                    userRepo.getCurrentUser().first { it is Result.Success } as Result.Success
                when (user1.data.userType) {
                    UserType.CUSTOMER -> get<CustomerPostDetailsVM>()
                    UserType.FIXER -> get<FixerPostDetailsVM>()
                }
            }
            isAssignableFrom(CategoryVM::class.java) -> get<CategoryVM>()
            isAssignableFrom(ProfileVM::class.java) -> get<ProfileVM>()
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    } as T
}

fun Fragment.getViewModelFactory(defaultArgs: Bundle? = null): ViewModelProvider.Factory =
    ViewModelFactory(this, defaultArgs)
