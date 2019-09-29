package com.findparking.app.repositories

import androidx.lifecycle.MutableLiveData
import com.findparking.app.api.HomeApi
import com.findparking.app.data.remote.responsebodies.HomeResponseBody
import com.findparking.app.models.network.NetworkState
import okhttp3.ResponseBody
import javax.inject.Inject

class HomeRepository
@Inject constructor(
    private val homeApi: HomeApi
) {

    val networkState = MutableLiveData<NetworkState>()

    suspend fun getHomeData(
        coordinates: String,
        original: String,
        onSuccess: suspend (response: HomeResponseBody) -> Unit,
        onError: suspend (ResponseBody?) -> Unit
    ) {
        networkState.postValue(NetworkState.LOADING)
        val response = homeApi.getHomeDataAsync(
            coordinates,
            original
        ).await()
        val responseBody = response.body()
        if (response.isSuccessful && responseBody != null) {
            networkState.postValue(NetworkState.SUCCESSFUL)
            onSuccess(responseBody)
        } else {
            networkState.postValue(NetworkState.error(response.errorBody()))
            onError(response.errorBody())
        }
        //requestCities(this.cities)
    }


}