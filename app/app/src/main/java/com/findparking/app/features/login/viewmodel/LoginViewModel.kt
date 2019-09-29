package com.findparking.app.features.login.viewmodel

import com.findparking.app.baseui.BaseViewModel
import com.findparking.app.data.local.AppDatabase
import com.findparking.app.data.local.database.entity.UserEntity
import com.findparking.app.data.local.sharedpreferences.SyncSharedPreferences
import com.findparking.app.repositories.UserRepository
import com.findparking.app.toolbox.extensions.logd
import com.findparking.app.toolbox.extensions.loge
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sp: SyncSharedPreferences,
    dao: AppDatabase
) : BaseViewModel() {

    private val mAuth = FirebaseAuth.getInstance()
    val userLiveData = dao.userDao().getUserLiveData()

    fun signInViaGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        mAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                logd("SignIn Presenter", "signInWithCredential:success")
                val fireBaseUser = FirebaseAuth.getInstance().currentUser
                fireBaseUser?.uid?.let { id ->
                    launch {
                        userRepository.setUser(
                            UserEntity(
                                id,
                                account.givenName,
                                account.familyName,
                                account.photoUrl?.path
                            )
                        )
                    }
                    sp.setCurrentUserId(id) // todo delete from here
                    sp.setLoggedIn(true)
                }
            } else {
                loge("SignIn Presenter", "signInWithCredential:failure", task.exception)
            }
        }
    }
}