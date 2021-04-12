package com.udacity.asteroidradar.api.network

import com.squareup.moshi.Moshi
import retrofit2.converter.moshi.MoshiConverterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.BuildConfig.API_KEY
import com.udacity.asteroidradar.api.models.NetworkImageOfDay
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.nasa.gov/"

private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()

interface ImageOfDayService {
    @GET("planetary/apod")
    suspend fun getImageOfDay(@Query("api_key") apiKey:String = API_KEY): NetworkImageOfDay
}

object NasaApi {
    val imageOfDayService = retrofit.create(ImageOfDayService::class.java)
}