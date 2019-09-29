package com.findparking.app.data.remote.responsebodies

import com.google.gson.annotations.SerializedName

data class HomeResponseBody(
    @SerializedName("adress") val adress: String,
    @SerializedName("occupation") val occupation: Double,
    @SerializedName("arrival_time") val arrival_time: String
)