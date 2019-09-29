package com.findparking.app.features.onboarding.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.findparking.app.AppViewModelsFactory
import com.findparking.app.R
import com.findparking.app.baseui.BaseFragment
import com.findparking.app.features.login.LoginActivity
import com.findparking.app.features.onboarding.fragments.pages.OnboardingPageAdapter
import com.findparking.app.features.onboarding.viewmodel.OnBoardViewModel
import com.findparking.app.toolbox.extensions.hideWithAnimationAlpha
import com.findparking.app.toolbox.extensions.showWithAnimationAlpha
import kotlinx.android.synthetic.main.fragment_on_boarding.*
import javax.inject.Inject

class OnBoardingFragment : BaseFragment(), View.OnClickListener {
    @Inject
    lateinit var vmFactory: AppViewModelsFactory
    private lateinit var viewModel: OnBoardViewModel

    companion object {
        val TAG = OnBoardingFragment::class.java.simpleName
        fun newInstance() = OnBoardingFragment()
    }

    override fun layoutId(): Int = R.layout.fragment_on_boarding

    override fun onViewReady(inflatedView: View, args: Bundle?) {
        initViewPager()
    }

    override fun setListeners() {
        tvSignWithGoogle.setOnClickListener(this)
        sign_in.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            sign_in -> {
                startLoginActivity()
            }
        }
    }

    private fun startLoginActivity() {
        viewModel.setOnboardingDone()
        startActivity(Intent(activity, LoginActivity::class.java))
        activity?.finish()
    }

    private fun initViewPager() {
        fragmentManager?.let { fragmentManager ->
            container.showWithAnimationAlpha()
            pager.adapter = OnboardingPageAdapter(fragmentManager)
            pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {}
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
                override fun onPageSelected(position: Int) {
                    if (position == 2) {
                        tvSignWithGoogle.hideWithAnimationAlpha()
                        sign_in.showWithAnimationAlpha()
                    } else {
                        tvSignWithGoogle.showWithAnimationAlpha()
                        sign_in.hideWithAnimationAlpha()
                    }
                }
            })
            dots_indicator.setViewPager(pager)
        }
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this, vmFactory).get(OnBoardViewModel::class.java)
    }
}
