package com.sunasterisk.thooi.di

import com.google.android.libraries.places.api.Places
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    factory { Places.createClient(androidContext()) }
}

val viewModelModule = module {
}
