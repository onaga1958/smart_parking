package com.findparking.app.features.launcher

import android.content.Intent
import android.os.Bundle
import com.findparking.app.AppViewModelsFactory
import com.findparking.app.baseui.BaseActivity
import com.findparking.app.data.local.result.EventObserver
import com.findparking.app.features.launcher.viewmodel.LaunchDestination
import com.findparking.app.features.launcher.viewmodel.LaunchViewModel
import com.findparking.app.features.main.MainActivity
import com.findparking.app.features.onboarding.OnboardingActivity
import viewModelProvider
import javax.inject.Inject

class LauncherActivity : BaseActivity() {

    @Inject
    lateinit var vmFactory: AppViewModelsFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: LaunchViewModel = viewModelProvider(vmFactory)
            viewModel.launchDestination.observe(this, EventObserver { destination ->
                when (destination) {
                    LaunchDestination.ONBOARDING -> startActivity(Intent(this, OnboardingActivity::class.java))
                    LaunchDestination.LOGIN -> startActivity(Intent(this, MainActivity::class.java))
                    LaunchDestination.MAIN_ACTIVITY -> startActivity(Intent(this, MainActivity::class.java))
                }
                this.finish()
            })
    }
}
