package com.findparking.app.di.modules

import com.findparking.app.AppExecutors
import com.findparking.app.data.local.AppDatabase
import com.findparking.app.api.UserApi
import com.findparking.app.repositories.UserRepository
import dagger.Module
import dagger.Provides

@Module
class RepositoriesModule {

    @Provides
    fun provideUserRepository(
        appDatabase: AppDatabase,
        appExecutors: AppExecutors,
        userApi: UserApi
    ): UserRepository {
        return UserRepository(appDatabase, appExecutors, userApi, appDatabase)
    }
}