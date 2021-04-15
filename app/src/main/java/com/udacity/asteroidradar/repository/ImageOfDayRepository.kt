package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.api.network.NasaApi
import com.udacity.asteroidradar.api.toDomainModel
import com.udacity.asteroidradar.database.AsteroidRadarDatabase
import com.udacity.asteroidradar.database.toDatabaseModel
import com.udacity.asteroidradar.database.toDomainModel
import com.udacity.asteroidradar.repository.models.ImageOfDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ImageOfDayRepository(private val database: AsteroidRadarDatabase) {

    val imageOfDay: LiveData<ImageOfDay> =
            Transformations.map(database.imageOfDayDao.getImageOfDay()) { imageEntity ->
                imageEntity?.toDomainModel()
            }

    suspend fun getNewImagesOfDay() {
        withContext(Dispatchers.IO) {
            try {
                val image = NasaApi.imageOfDayService.getImageOfDay()
                val domainImage = image.toDomainModel()
                if (domainImage.mediaType == "image") {
                    database.imageOfDayDao.clear()
                    database.imageOfDayDao.insertAll(domainImage.toDatabaseModel())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}