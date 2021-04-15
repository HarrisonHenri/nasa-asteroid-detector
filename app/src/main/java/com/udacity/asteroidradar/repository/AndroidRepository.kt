package com.udacity.asteroidradar.repository

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.api.getOneWeekAheadDateFormatted
import com.udacity.asteroidradar.api.getTodayDateFormatted
import com.udacity.asteroidradar.api.network.NasaApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidRadarDatabase
import com.udacity.asteroidradar.database.toDatabaseModel
import com.udacity.asteroidradar.database.toDomainModel
import com.udacity.asteroidradar.repository.models.Asteroid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidRepository (private val database: AsteroidRadarDatabase)
{

    @RequiresApi(Build.VERSION_CODES.O)
    val asteroids : LiveData<List<Asteroid>> = Transformations.map(database.asteroidDao.getSavedAsteroids()) { asteroidEntities ->
        asteroidEntities.toDomainModel()
    }


    suspend fun getNewAsteroids(){
        withContext(Dispatchers.IO){
            try{
                val startDate = getTodayDateFormatted()
                val endDate = getOneWeekAheadDateFormatted()
                val asteroidsResult  = NasaApi.asteroidsListService.getAsteroidsList(startDate , endDate)
                val parsedAsteroids = parseAsteroidsJsonResult(JSONObject(asteroidsResult))
                database.asteroidDao.insertAll(*parsedAsteroids.toDatabaseModel())
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}