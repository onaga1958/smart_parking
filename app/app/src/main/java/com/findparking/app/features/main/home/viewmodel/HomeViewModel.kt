package com.findparking.app.features.main.home.viewmodel

import androidx.lifecycle.MutableLiveData
import com.findparking.app.baseui.BaseViewModel
import com.findparking.app.data.entities.CollectionWithBooks
import com.findparking.app.data.remote.responsebodies.HomeResponseBody
import com.findparking.app.repositories.HomeRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : BaseViewModel() {

    val collectionWithBooks by lazy { MutableLiveData<HomeResponseBody>() }

    fun getHomeData(
        coordinates:String,
        original:String
    ) {
        launch {
            homeRepository.getHomeData(
                coordinates,
                original,
                {
                    collectionWithBooks.postValue(it)
                },
                {

                })
        }
    }
}