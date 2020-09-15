package com.sunasterisk.thooi.di

import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sunasterisk.thooi.data.repository.NotificationRepository
import com.sunasterisk.thooi.data.repository.NotificationRepositoryImpl
import com.sunasterisk.thooi.data.repository.UserRepository
import com.sunasterisk.thooi.data.repository.UserRepositoryImpl
import com.sunasterisk.thooi.data.source.NotificationDataSource
import com.sunasterisk.thooi.data.source.UserDataSource
import com.sunasterisk.thooi.data.source.local.NotificationLocalDataSource
import com.sunasterisk.thooi.data.source.local.UserLocalDataSource
import com.sunasterisk.thooi.data.source.local.database.AppDataBase
import com.sunasterisk.thooi.data.source.local.database.DatabaseConstants
import com.sunasterisk.thooi.data.source.remote.NotificationRemoteDataSource
import com.sunasterisk.thooi.data.source.remote.UserRemoteDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val repositoryModule = module {

    //Firebase modules
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }

    //Room database modules
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDataBase::class.java,
            DatabaseConstants.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    //Room Dao modules
    single { get<AppDataBase>().userDao() }

    //DataSource modules
    single<UserDataSource.Remote> { UserRemoteDataSource(get(), get()) }
    single<UserDataSource.Local> { UserLocalDataSource(get(), get()) }
    single<NotificationDataSource.Remote> { NotificationRemoteDataSource(get(), get()) }
    single<NotificationDataSource.Local> { NotificationLocalDataSource() } //FIXME: Not implemented yet

    //Repository modules
    single<UserRepository> { UserRepositoryImpl(get(), get()) }
    single<NotificationRepository> { NotificationRepositoryImpl(get(), get(), get()) }
}
