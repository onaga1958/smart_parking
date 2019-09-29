package com.findparking.app.features.login.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.findparking.app.AppViewModelsFactory
import com.findparking.app.R
import com.findparking.app.baseui.BaseFragment
import com.findparking.app.features.login.viewmodel.LoginViewModel
import com.findparking.app.features.main.MainActivity
import com.findparking.app.toolbox.extensions.logd
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.android.synthetic.main.fragment_login.*
import viewModelProvider
import javax.inject.Inject

class LoginFragment : BaseFragment(), View.OnClickListener {
    @Inject
    lateinit var vmFactory: AppViewModelsFactory
    private lateinit var viewModel: LoginViewModel

    private lateinit var googleSignInClient: GoogleSignInClient

    companion object {
        val TAG = LoginFragment::class.java.simpleName
        fun newInstance() = LoginFragment()
    }

    private val RC_SIGN_IN = 1

    override fun layoutId(): Int = R.layout.fragment_login

    override fun onViewReady(inflatedView: View, args: Bundle?) {
        setListeners()
    }

    override fun initViewModel() {
        viewModel = viewModelProvider(vmFactory)
        viewModel.userLiveData.observe(this, Observer {
            if (it != null) {
                startMainActivity()
            }
        })
    }

    override fun setListeners() {
        tvSignWithGoogle.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            tvSignWithGoogle -> {
                signWithGoogle()
            }
        }
    }

    private fun signWithGoogle() {
        context?.let { safeContext ->
            // Configure Google Sign In
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            googleSignInClient = GoogleSignIn.getClient(safeContext, gso)

            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    private fun startMainActivity() {
        startActivity(Intent(activity, MainActivity::class.java))
        activity?.finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                account?.let {
                    viewModel.signInViaGoogle(account)
                }
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                logd("GoogleSignIn", "Google sign in failed")
            }
        }
    }
}