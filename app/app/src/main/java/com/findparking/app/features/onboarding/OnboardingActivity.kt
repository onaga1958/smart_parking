package com.findparking.app.features.onboarding

import android.os.Bundle
import com.findparking.app.R
import com.findparking.app.baseui.BaseActivity
import com.findparking.app.features.onboarding.fragments.OnBoardingFragment

class OnboardingActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
        addFragment()
    }
    private fun addFragment() {
        supportFragmentManager.beginTransaction()
            .add(
                R.id.fragmentContainer,
                OnBoardingFragment.newInstance(),
                OnBoardingFragment.TAG
            )
            .commit()
    }
}
