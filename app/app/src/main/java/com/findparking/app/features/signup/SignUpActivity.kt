package com.findparking.app.features.signup

import android.os.Bundle
import com.findparking.app.R
import com.findparking.app.baseui.BaseActivity
import com.findparking.app.features.signup.fragments.SignUpFragment

class SignUpActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        addFragment()
    }
    private fun addFragment() {
        supportFragmentManager.beginTransaction()
            .add(
                R.id.fragmentContainer,
                SignUpFragment.newInstance(),
                SignUpFragment.TAG
            )
            .commit()
    }
}