package com.findparking.app.di.modules

import com.findparking.app.AppExecutors
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ExecutorsModule {

    @Provides
    @Singleton
    fun provideAppExecutors(): AppExecutors {
        return AppExecutors(2)
    }
}