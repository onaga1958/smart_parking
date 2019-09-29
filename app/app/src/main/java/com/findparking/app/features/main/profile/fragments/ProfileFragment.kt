package com.findparking.app.features.main.profile.fragments

import android.os.Bundle
import android.view.View
import com.findparking.app.R
import com.findparking.app.ToolbarFragment
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : ToolbarFragment() {
    companion object {
        val TAG = ProfileFragment::class.java.simpleName
        fun newInstance() = ProfileFragment()
    }

    override fun layoutId(): Int = R.layout.fragment_profile

    override fun setToolbar() {
        toolbar.inflateMenu(R.menu.profile_navigation)
    }

    override fun onViewReady(inflatedView: View, args: Bundle?) {
        setViewPager()
    }

    private fun setViewPager(){
        fragmentManager?.let {safeFragmentManager ->
            viewpager.adapter = ProfilePageAdapter(safeFragmentManager)
            tabs.setupWithViewPager(viewpager)
        }
    }

    private fun setData() {
//        profile_photo.loadWithGlideCircleCrop(
//            "https://i.pinimg.com/originals/ce/10/84/ce1084ba5e7d8342b59e347f2684d99f.jpg",
//            R.drawable.fab_ripple
//        )
    }

    override fun initViewModel() {
    }

    override fun setListeners() {
    }
}
