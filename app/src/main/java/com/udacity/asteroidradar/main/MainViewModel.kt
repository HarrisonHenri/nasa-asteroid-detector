package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.api.models.NetworkImageOfDay
import com.udacity.asteroidradar.api.network.NasaApi
import kotlinx.coroutines.launch


class MainViewModel : ViewModel() {
    private val _imageOfDay = MutableLiveData<NetworkImageOfDay>()
    val imageOfDay: LiveData<NetworkImageOfDay>
        get() = _imageOfDay

    private val _imageOfDayStatus = MutableLiveData<String>()
    val imageOfDayStatus: LiveData<String>
        get() = _imageOfDayStatus

    init {
        getImageOfTheDay()
    }

    private fun getImageOfTheDay() {
        viewModelScope.launch {
            try {
                val result = NasaApi.imageOfDayService.getImageOfDay()
                if (result.mediaType == "video") {
                    result.url = "https://apod.nasa.gov/apod/image/2001/STSCI-H-p2006a-h-1024x614.jpg"
                }
                _imageOfDay.value = result
            } catch (e: Exception) {
                _imageOfDayStatus.value = "Failure: ${e.message}"
            }
        }
    }

}