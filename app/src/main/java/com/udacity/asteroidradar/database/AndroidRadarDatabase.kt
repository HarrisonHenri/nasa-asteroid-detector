package com.udacity.asteroidradar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.udacity.asteroidradar.database.dao.AsteroidDao
import com.udacity.asteroidradar.database.dao.ImageOfDayDao
import com.udacity.asteroidradar.database.entities.AsteroidEntity
import com.udacity.asteroidradar.database.entities.ImageOfDayEntity

@Database(entities = [AsteroidEntity::class, ImageOfDayEntity::class], version = 1)
abstract class AsteroidRadarDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
    abstract val imageOfDayDao: ImageOfDayDao
}

private lateinit var INSTANCE: AsteroidRadarDatabase

fun getDatabase(context: Context): AsteroidRadarDatabase {
    synchronized(AsteroidRadarDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                    AsteroidRadarDatabase::class.java,
                    "asteroids").build()
        }
    }
    return INSTANCE
}