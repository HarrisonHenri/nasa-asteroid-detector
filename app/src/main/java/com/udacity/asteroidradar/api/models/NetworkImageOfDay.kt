package com.udacity.asteroidradar.api.models

import com.squareup.moshi.Json

data class NetworkImageOfDay(@Json(name = "media_type") val mediaType: String, val title: String, var url: String)
