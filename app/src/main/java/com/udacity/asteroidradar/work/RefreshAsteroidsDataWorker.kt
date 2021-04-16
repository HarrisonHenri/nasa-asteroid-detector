package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import com.udacity.asteroidradar.repository.ImageOfDayRepository
import retrofit2.HttpException

class RefreshAsteroidsDataWorker(appContext: Context, params: WorkerParameters): CoroutineWorker(appContext, params) {
    companion object {
        const val WORK_NAME = "RefreshAsteroidsDataWorker"
    }

    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val asteroidRepository = AsteroidRepository(database)
        val imageRepository = ImageOfDayRepository(database)

        return try {
            imageRepository.getNewImagesOfDay()
            asteroidRepository.getNewAsteroids()

            Result.success()
        } catch (exc: HttpException) {
            Result.retry()
        }
    }
}