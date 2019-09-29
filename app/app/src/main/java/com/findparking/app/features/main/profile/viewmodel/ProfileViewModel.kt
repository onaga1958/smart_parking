package com.findparking.app.features.main.profile.viewmodel

import androidx.lifecycle.ViewModel
import com.findparking.app.repositories.UserRepository
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel()