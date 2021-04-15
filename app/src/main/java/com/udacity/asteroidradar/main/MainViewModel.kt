package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import com.udacity.asteroidradar.repository.ImageOfDayRepository
import kotlinx.coroutines.launch


class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val imageOfDayRepository = ImageOfDayRepository(database)
    private val asteroidRepository = AsteroidRepository(database)

    init {
        viewModelScope.launch {
            asteroidRepository.getNewAsteroids()
            imageOfDayRepository.getNewImagesOfDay()
        }
    }

    val imageOfDay = imageOfDayRepository.imageOfDay
    val asteroidList = asteroidRepository.asteroids

}