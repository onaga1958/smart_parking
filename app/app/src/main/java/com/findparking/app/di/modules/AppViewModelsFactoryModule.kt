package com.findparking.app.di.modules

import com.findparking.app.AppViewModelsFactory
import com.findparking.app.di.AppViewModelsComponent
import dagger.Module
import dagger.Provides

@Module(subcomponents = [AppViewModelsComponent::class])
class AppViewModelsFactoryModule {

    @Provides
    fun provideAppViewModelFactory(builder: AppViewModelsComponent.Builder): AppViewModelsFactory {
        return AppViewModelsFactory(builder.build())
    }
}