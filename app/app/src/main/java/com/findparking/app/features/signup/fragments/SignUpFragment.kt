package com.findparking.app.features.signup.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.findparking.app.AppViewModelsFactory
import com.findparking.app.R
import com.findparking.app.ToolbarFragment
import com.findparking.app.features.signup.viewmodel.SignUpViewModel
import javax.inject.Inject

class SignUpFragment : ToolbarFragment(), View.OnClickListener {
    override fun setToolbar() {
    }

    @Inject
    lateinit var vmFactory: AppViewModelsFactory
    private lateinit var viewModel: SignUpViewModel

    companion object {
        val TAG = SignUpFragment::class.java.simpleName
        fun newInstance() = SignUpFragment()
    }

    override fun layoutId(): Int = R.layout.fragment_sign_up

    override fun onViewReady(inflatedView: View, args: Bundle?) {
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders
            .of(this, vmFactory)
            .get(SignUpViewModel::class.java)
    }

    override fun setListeners() {
    }

    override fun onClick(v: View?) {
    }
}
