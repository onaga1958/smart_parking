package com.findparking.app.di.modules

import com.findparking.app.features.launcher.LauncherActivity
import com.findparking.app.features.login.LoginActivity
import com.findparking.app.features.main.MainActivity
import com.findparking.app.features.onboarding.OnboardingActivity
import com.findparking.app.features.signup.SignUpActivity

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ActivityBuilder {

    @ContributesAndroidInjector
    fun contributeLauncher(): LauncherActivity

    @ContributesAndroidInjector
    fun contributeLogin(): LoginActivity

    @ContributesAndroidInjector
    fun contributeMain(): MainActivity

    @ContributesAndroidInjector
    fun contributeOnboarding(): OnboardingActivity

    @ContributesAndroidInjector
    fun contributeSignUp(): SignUpActivity
}