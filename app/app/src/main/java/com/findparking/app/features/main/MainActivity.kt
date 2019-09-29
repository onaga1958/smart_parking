package com.findparking.app.features.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.findparking.app.R
import com.findparking.app.baseui.BaseActivity
import com.findparking.app.features.main.home.fragments.HomeFragment
import com.findparking.app.features.main.profile.fragments.ProfileFragment

class MainActivity : BaseActivity(), View.OnClickListener {

    override fun onClick(v: View?) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addFirstFragment()
    }

    private fun setListeners() {

    }

    private fun addFirstFragment() {
        setFragment(HomeFragment.newInstance(), R.id.mainContainer)
    }

    private fun startFragment(fr: Fragment, tag: String) {
        setFragment(fr, R.id.navigationFragmentContainer)
    }

}
