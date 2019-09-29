package com.findparking.app.features.onboarding.fragments.pages

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.findparking.app.R
import com.findparking.app.models.IntroModel

class OnboardingPageAdapter(
    fm: FragmentManager
) : FragmentPagerAdapter(fm) {

    private val introList = listOf(
        IntroModel(
            0,
            "You can read anywhere",
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry.",
            R.drawable.ill_undraw_subway
        ),
        IntroModel(
            1,
            "Learn faster and fun",
            " Lorem Ipsum has been the industry's standard dummy text ever since the",
            R.drawable.ill_undraw_book_lover
        ),
        IntroModel(
            1,
            "Lear faster and fun",
            "Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of classical Latin literature from 45 BC, making it over 2000 years old",
            R.drawable.ill_undraw_contemplating
        )
    )

    private fun getFragment(position: Int): Fragment {
        val bundle = Bundle()
        bundle.putString("title", introList[position].title)
        bundle.putString("description", introList[position].description)
        bundle.putInt("illustration", introList[position].illustration)

        val fragment2 = OnBoardingPage()
        fragment2.arguments = bundle
        return fragment2
    }

    override fun getCount(): Int = introList.size

    override fun getItem(position: Int): Fragment = getFragment(position)
}