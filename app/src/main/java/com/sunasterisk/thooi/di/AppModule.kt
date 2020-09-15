package com.sunasterisk.thooi.di

import com.google.android.libraries.places.api.Places
import com.sunasterisk.thooi.ui.category.CategoryVM
import com.sunasterisk.thooi.ui.home.HomeVM
import com.sunasterisk.thooi.ui.main.MainVM
import com.sunasterisk.thooi.ui.notification.notifications.NotificationViewModel
import com.sunasterisk.thooi.ui.post.detail.CustomerPostDetailsVM
import com.sunasterisk.thooi.ui.post.detail.FixerPostDetailsVM
import com.sunasterisk.thooi.ui.profile.ProfileVM
import com.sunasterisk.thooi.ui.signin.SignInViewModel
import com.sunasterisk.thooi.ui.signup.SignUpViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    factory { Places.createClient(androidContext()) }
}

val viewModelModule = module {
    viewModel { SignInViewModel(get()) }
    viewModel { NotificationViewModel(get(), get()) }
    viewModel { SignUpViewModel(get(), get()) }
    viewModel { MainVM(get()) }
    viewModel { HomeVM(get(), get(), get()) }
    viewModel { CustomerPostDetailsVM(get()) }
    viewModel { FixerPostDetailsVM(get()) }
    viewModel { CategoryVM(get(), get(), get()) }
    viewModel { ProfileVM(get()) }
}
