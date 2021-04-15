package com.udacity.asteroidradar.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.database.entities.ImageOfDayEntity

@Dao
interface ImageOfDayDao {
    @Query("select * from image_of_day_table")
    fun getImageOfDay() : LiveData<ImageOfDayEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg imageEntity: ImageOfDayEntity)

    @Query("delete from image_of_day_table")
    fun  clear()
}