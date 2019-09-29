package com.findparking.app.features.login

import android.os.Bundle
import com.findparking.app.R
import com.findparking.app.baseui.BaseActivity
import com.findparking.app.features.login.fragments.LoginFragment

class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        addFragment()
    }

    private fun addFragment() {
        supportFragmentManager.beginTransaction()
            .add(
                R.id.fragmentContainer,
                LoginFragment.newInstance(),
                LoginFragment.TAG
            )
            .commit()
    }
}
