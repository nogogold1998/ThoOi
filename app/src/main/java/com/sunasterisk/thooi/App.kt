package com.sunasterisk.thooi

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import com.sunasterisk.thooi.di.appModule
import com.sunasterisk.thooi.di.repositoryModule
import com.sunasterisk.thooi.di.viewModelModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    @ExperimentalCoroutinesApi
    override fun onCreate() {
        super.onCreate()

        AndroidThreeTen.init(this)

        startKoin {
            androidContext(this@App)
            modules(repositoryModule, viewModelModule, appModule)
        }
    }
}
