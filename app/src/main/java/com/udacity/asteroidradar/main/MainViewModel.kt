package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.api.getOneWeekAheadDateFormatted
import com.udacity.asteroidradar.api.getTodayDateFormatted
import com.udacity.asteroidradar.api.models.NetworkImageOfDay
import com.udacity.asteroidradar.api.network.NasaApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.models.Asteroid
import kotlinx.coroutines.launch
import org.json.JSONObject


class MainViewModel : ViewModel() {
    private val _imageOfDay = MutableLiveData<NetworkImageOfDay>()
    val imageOfDay: LiveData<NetworkImageOfDay>
        get() = _imageOfDay

    private val _imageOfDayStatus = MutableLiveData<String>()
    val imageOfDayStatus: LiveData<String>
        get() = _imageOfDayStatus

    private val _asteroidList = MutableLiveData<ArrayList<Asteroid>>()
    val asteroidList: LiveData<ArrayList<Asteroid>>
        get() = _asteroidList

    private val _asteroidListStatus = MutableLiveData<String>()
    val asteroidListStatus: LiveData<String>
        get() = _asteroidListStatus

    init {
        getImageOfTheDay()
        getAsteroidsList()
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

    private fun getAsteroidsList() {
        viewModelScope.launch {
            try {
                val startDate = getTodayDateFormatted()
                val endDate = getOneWeekAheadDateFormatted()
                val result = NasaApi.asteroidsListService.getAsteroidsList(startDate, endDate)
                _asteroidList.value = parseAsteroidsJsonResult(JSONObject(result))
            } catch (e: Exception) {
                _asteroidListStatus.value = "Failure: ${e.message}"
            }
        }
    }

}