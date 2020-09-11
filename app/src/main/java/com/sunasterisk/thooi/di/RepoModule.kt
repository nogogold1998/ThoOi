package com.sunasterisk.thooi.di

import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sunasterisk.thooi.data.repository.FakePostDetailRepo
import com.sunasterisk.thooi.data.repository.PostDetailRepository
import com.sunasterisk.thooi.data.repository.UserRepository
import com.sunasterisk.thooi.data.repository.UserRepositoryImpl
import com.sunasterisk.thooi.data.source.UserDataSource
import com.sunasterisk.thooi.data.source.entity.UserType
import com.sunasterisk.thooi.data.source.local.UserLocalDataSource
import com.sunasterisk.thooi.data.source.local.database.AppDataBase
import com.sunasterisk.thooi.data.source.local.database.DatabaseConstants
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

    //Repository modules
    single<UserRepository> { UserRepositoryImpl(get(), get()) }

    single<PostDetailRepository> { FakePostDetailRepo() }
    single { UserType.CUSTOMER }
}
