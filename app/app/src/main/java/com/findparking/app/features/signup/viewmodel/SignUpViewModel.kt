package com.findparking.app.features.signup.viewmodel

import androidx.lifecycle.ViewModel
import com.findparking.app.repositories.UserRepository
import javax.inject.Inject

class SignUpViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel()