package com.findparking.app.di.modules

import com.findparking.app.features.login.fragments.LoginFragment
import com.findparking.app.features.main.home.fragments.HomeFragment
import com.findparking.app.features.main.profile.fragments.ProfileFragment
import com.findparking.app.features.onboarding.fragments.OnBoardingFragment
import com.findparking.app.features.signup.fragments.SignUpFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface FragmentBuilder {
    @ContributesAndroidInjector()
    fun provideHiveFragment(): HomeFragment

    @ContributesAndroidInjector()
    fun provideFavoriteFragment(): ProfileFragment

    @ContributesAndroidInjector()
    fun provideOnBoardingFragment(): OnBoardingFragment

    @ContributesAndroidInjector
    fun provideSignUpFragment(): SignUpFragment

    @ContributesAndroidInjector
    fun provideLoginFragment(): LoginFragment
}