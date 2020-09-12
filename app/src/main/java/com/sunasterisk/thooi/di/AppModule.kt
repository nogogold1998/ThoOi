package com.sunasterisk.thooi.di

import com.google.android.libraries.places.api.Places
import com.sunasterisk.thooi.data.repository.FakeCategoryRepo
import com.sunasterisk.thooi.data.repository.FakePostRepo
import com.sunasterisk.thooi.ui.home.HomeVM
import com.sunasterisk.thooi.ui.notification.notifications.NotificationViewModel
import com.sunasterisk.thooi.ui.placespicker.AddressViewModel
import com.sunasterisk.thooi.ui.post.newpost.NewPostViewModel
import com.sunasterisk.thooi.ui.settings.SettingsViewModel
import com.sunasterisk.thooi.ui.signin.SignInViewModel
import com.sunasterisk.thooi.ui.signup.SignUpViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    factory { Places.createClient(androidContext()) }
}

val viewModelModule = module {
    viewModel { SignInViewModel(get(), get()) }
    viewModel { NotificationViewModel(get(), get()) }
    viewModel { SignUpViewModel(get(), get()) }
    viewModel { HomeVM(FakeCategoryRepo(), FakePostRepo()) }
    viewModel { SettingsViewModel(get(), get(), get()) }
    viewModel { AddressViewModel(get()) }
    viewModel { NewPostViewModel(get()) }
}
