package com.findparking.app.data.local.sharedpreferences

interface AppSharedPreferences {

    fun isLoggedIn(): Boolean
    fun setLoggedIn(loggedIn: Boolean)

    fun isOnboardingPassed(): Boolean
    fun setOnBoardingPassed(passed: Boolean)

    fun getCurrentUserId(): String
    fun setCurrentUserId(id: String)
}