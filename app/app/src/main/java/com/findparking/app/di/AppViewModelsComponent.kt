package com.findparking.app.di

import com.findparking.app.features.launcher.viewmodel.LaunchViewModel
import com.findparking.app.features.login.viewmodel.LoginViewModel
import com.findparking.app.features.main.home.viewmodel.HomeViewModel
import com.findparking.app.features.onboarding.viewmodel.OnBoardViewModel
import com.findparking.app.features.signup.viewmodel.SignUpViewModel
import dagger.Subcomponent

@Subcomponent
interface AppViewModelsComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build(): AppViewModelsComponent
    }

    fun provideLaunchViewModel(): LaunchViewModel
    fun provideOnBoardViewModel(): OnBoardViewModel
    fun provideLoginViewModel(): LoginViewModel
    fun provideSignUpViewModel(): SignUpViewModel
    fun provideHomeViewModel(): HomeViewModel
}