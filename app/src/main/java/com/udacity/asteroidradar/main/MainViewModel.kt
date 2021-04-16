package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import com.udacity.asteroidradar.repository.ImageOfDayRepository
import com.udacity.asteroidradar.repository.models.Asteroid
import kotlinx.coroutines.launch


class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val imageOfDayRepository = ImageOfDayRepository(database)
    private val asteroidRepository = AsteroidRepository(database)

    private val _navigateToAsteroidDetails = MutableLiveData<Asteroid>()
    val navigateToAsteroidDetails: LiveData<Asteroid>
        get() = _navigateToAsteroidDetails

    init {
        viewModelScope.launch {
            asteroidRepository.getNewAsteroids()
            imageOfDayRepository.getNewImagesOfDay()
        }
    }

    val imageOfDay = imageOfDayRepository.imageOfDay
    val asteroidList = asteroidRepository.asteroids

    fun asteroidClicked(asteroid: Asteroid){
        _navigateToAsteroidDetails.value = asteroid
    }

    fun doneNavigation(){
        _navigateToAsteroidDetails.value = null
    }

}