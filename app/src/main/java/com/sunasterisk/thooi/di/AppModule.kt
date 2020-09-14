package com.sunasterisk.thooi.di

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.libraries.places.api.Places
import com.sunasterisk.thooi.R
import com.sunasterisk.thooi.ui.category.CategoryVM
import com.sunasterisk.thooi.ui.home.HomeVM
import com.sunasterisk.thooi.ui.main.MainVM
import com.sunasterisk.thooi.ui.notification.notifications.NotificationViewModel
import com.sunasterisk.thooi.ui.placespicker.AddressViewModel
import com.sunasterisk.thooi.ui.post.detail.CustomerPostDetailsVM
import com.sunasterisk.thooi.ui.post.detail.FixerPostDetailsVM
import com.sunasterisk.thooi.ui.post.newpost.NewPostViewModel
import com.sunasterisk.thooi.ui.settings.SettingsViewModel
import com.sunasterisk.thooi.ui.signin.SignInViewModel
import com.sunasterisk.thooi.ui.signup.SignUpViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    factory { Places.createClient(androidContext()) }
    factory {
        GoogleSignIn.getClient(
            androidContext(), GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(androidContext().getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        )
    }
}

val viewModelModule = module {
    viewModel { SignInViewModel(get(), get()) }
    viewModel { NotificationViewModel(get(), get()) }
    viewModel { SignUpViewModel(get(), get()) }
    viewModel { SettingsViewModel(get(), get(), get(), get()) }
    viewModel { AddressViewModel(get()) }
    viewModel { NewPostViewModel(get()) }
    viewModel { MainVM(get(), get()) }
    viewModel { HomeVM(get(), get()) }
    viewModel { CustomerPostDetailsVM(get()) }
    viewModel { FixerPostDetailsVM(get()) }
    viewModel { CategoryVM(get(), get(), get()) }
}
