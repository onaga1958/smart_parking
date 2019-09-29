package com.findparking.app.api

data class FollowRequestBody(
    val destination: String,
    val origin: String
)