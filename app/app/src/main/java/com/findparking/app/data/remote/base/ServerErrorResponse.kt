package com.findparking.app.data.remote.base

import com.google.gson.annotations.SerializedName
import com.findparking.app.models.network.CommonServerError

data class ServerErrorResponse(
    @SerializedName("success") val isSuccess: Boolean,
    @SerializedName("error") val serverError: CommonServerError?
)