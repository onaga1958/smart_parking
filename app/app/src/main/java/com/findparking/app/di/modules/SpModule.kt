package com.findparking.app.di.modules

import android.app.Application
import com.findparking.app.data.local.sharedpreferences.SyncSharedPreferences
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SpModule {

    @Singleton
    @Provides
    fun provideSyncSharedPreferences(app: Application): SyncSharedPreferences {
        return SyncSharedPreferences(app)
    }
}