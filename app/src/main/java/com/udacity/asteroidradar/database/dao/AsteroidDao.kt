package com.udacity.asteroidradar.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.database.entities.AsteroidEntity

@Dao
interface AsteroidDao {

    @Query("select * from asteroid_table order by date(closeApproachDate) asc")
    fun getCachedAsteroids(): LiveData<List<AsteroidEntity>>

    @Query("select * from asteroid_table where closeApproachDate = :date")
    fun getDailyAsteroids(date: String): LiveData<List<AsteroidEntity>>

    @Query("select * from asteroid_table where closeApproachDate between :startDate and :endDate order by date(closeApproachDate) asc")
    fun getWeeklyAsteroids(startDate: String, endDate: String) : LiveData<List<AsteroidEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg asteroids: AsteroidEntity)
}