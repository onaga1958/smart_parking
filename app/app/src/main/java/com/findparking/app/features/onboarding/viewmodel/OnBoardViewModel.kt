package com.findparking.app.features.onboarding.viewmodel

import androidx.lifecycle.ViewModel
import com.findparking.app.data.local.sharedpreferences.SyncSharedPreferences
import javax.inject.Inject

class OnBoardViewModel @Inject constructor(
    private val sp: SyncSharedPreferences
) : ViewModel() {

    fun setOnboardingDone() {
        sp.setOnBoardingPassed(true)
    }
}