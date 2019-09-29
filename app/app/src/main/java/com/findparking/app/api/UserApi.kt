package com.findparking.app.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface UserApi {
    @GET("getUser")
    fun getUser(
        @Query("assets") assets: Array<String>,
        @Query("assets_values") assets_values: Array<String>,
        @Query("date") date: Long
    ): Call<ResponseBody>
}