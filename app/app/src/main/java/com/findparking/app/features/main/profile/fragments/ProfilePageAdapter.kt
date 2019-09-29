package com.findparking.app.features.main.profile.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.findparking.app.data.local.ui.PageModel
import com.findparking.app.features.signup.fragments.SignUpFragment

class ProfilePageAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    val fragments = listOf(
        PageModel("Finished", SignUpFragment()),
        PageModel("Current books", SignUpFragment()),
        PageModel("Wish list", SignUpFragment())
    )

    override fun getItem(position: Int): Fragment {
        return SignUpFragment()
    }

    override fun getCount(): Int = fragments.size

    override fun getPageTitle(position: Int): CharSequence {
        return fragments[position].title
    }
}