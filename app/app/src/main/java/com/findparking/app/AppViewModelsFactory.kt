package com.findparking.app

import androidx.collection.ArrayMap
import androidx.lifecycle.ViewModel
import com.findparking.app.di.AppViewModelsComponent
import com.findparking.app.features.launcher.viewmodel.LaunchViewModel
import com.findparking.app.features.login.viewmodel.LoginViewModel
import com.findparking.app.features.main.home.viewmodel.HomeViewModel
import com.findparking.app.features.onboarding.viewmodel.OnBoardViewModel
import com.findparking.app.features.signup.viewmodel.SignUpViewModel
import java.util.concurrent.Callable

class AppViewModelsFactory(private val appViewModelsComponent: AppViewModelsComponent) :
    BaseViewModelFactory() {

    override fun fillViewModels(creators: ArrayMap<Class<*>, Callable<out ViewModel>>) {
        creators[LaunchViewModel::class.java] = Callable { appViewModelsComponent.provideLaunchViewModel() }
        creators[OnBoardViewModel::class.java] = Callable { appViewModelsComponent.provideOnBoardViewModel() }
        creators[LoginViewModel::class.java] = Callable { appViewModelsComponent.provideLoginViewModel() }
        creators[SignUpViewModel::class.java] = Callable { appViewModelsComponent.provideSignUpViewModel() }
        creators[HomeViewModel::class.java] = Callable { appViewModelsComponent.provideHomeViewModel() }
    }
}