package com.findparking.app.api

import com.findparking.app.data.remote.responsebodies.HomeResponseBody
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface HomeApi {
    @GET("api/find_parkings/")
    fun getHomeDataAsync(
        @Query("coordinates") coordinates:String,
        @Query("origin") origin: String
    ): Deferred<Response<HomeResponseBody>>
}