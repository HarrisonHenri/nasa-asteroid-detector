package com.udacity.asteroidradar.repository

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Constants.FrequencyFilter
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
import java.time.LocalDate


class AsteroidRepository(private val database: AsteroidRadarDatabase)
{
    private val frequencyFilter = MutableLiveData(FrequencyFilter.WEEKLY)

    @RequiresApi(Build.VERSION_CODES.O)
    private val _startDate = LocalDate.now()

    @RequiresApi(Build.VERSION_CODES.O)
    private val _endDate = _startDate.plusDays(7)

    @RequiresApi(Build.VERSION_CODES.O)
    val asteroids : LiveData<List<Asteroid>> = Transformations.switchMap(frequencyFilter)
    {
        when(it){
            FrequencyFilter.WEEKLY ->
                Transformations.map(database.asteroidDao.getWeeklyAsteroids(_startDate.toString(), _endDate.toString())) { asteroidEntities ->
                    asteroidEntities.toDomainModel()
                }
            FrequencyFilter.TODAY ->
                Transformations.map(database.asteroidDao.getDailyAsteroids(_startDate.toString())) {asteroidEntities ->
                    asteroidEntities.toDomainModel()
                }
            FrequencyFilter.CACHED ->
                Transformations.map(database.asteroidDao.getCachedAsteroids()) { asteroidEntities ->
                    asteroidEntities.toDomainModel()
                }

            else -> throw IllegalArgumentException("")
        }
    }

    fun changeFrequencyFilter(filter: FrequencyFilter){
        frequencyFilter.value = filter
    }

    suspend fun getNewAsteroids(){
        withContext(Dispatchers.IO){
            try{
                val startDate = getTodayDateFormatted()
                val endDate = getOneWeekAheadDateFormatted()
                val asteroidsResult  = NasaApi.asteroidsListService.getAsteroidsList(startDate, endDate)
                val parsedAsteroids = parseAsteroidsJsonResult(JSONObject(asteroidsResult))
                database.asteroidDao.insertAll(*parsedAsteroids.toDatabaseModel())
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}

