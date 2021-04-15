package com.udacity.asteroidradar.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "image_of_day_table")
data class ImageOfDayEntity(@PrimaryKey(autoGenerate = true) val id: Long=0L,
                            val mediaType: String, val title: String, val url: String)