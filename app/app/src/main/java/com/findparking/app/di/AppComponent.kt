package com.findparking.app.di

import android.app.Application
import com.findparking.app.BaseApplication
import com.findparking.app.di.modules.AppViewModelsFactoryModule
import com.findparking.app.di.modules.DbModule
import com.findparking.app.di.modules.ExecutorsModule
import com.findparking.app.di.modules.RepositoriesModule
import com.findparking.app.di.modules.SpModule
import com.findparking.app.di.modules.NetworkModule
import com.findparking.app.di.modules.AppApiModule
import com.findparking.app.di.modules.ActivityBuilder
import com.findparking.app.di.modules.FragmentBuilder
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/*Global Component*/
@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppViewModelsFactoryModule::class,
        DbModule::class,
        ExecutorsModule::class,
        RepositoriesModule::class,
        SpModule::class,
        NetworkModule::class,
        AppApiModule::class,

        ActivityBuilder::class,
        FragmentBuilder::class
    ]
)
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app: Application): Builder

        fun build(): AppComponent
    }

    fun inject(app: BaseApplication)
}