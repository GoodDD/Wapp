package com.example.wapp.data.remote

import com.example.wapp.BuildConfig
import com.example.wapp.data.models.Forecast
import com.example.wapp.data.models.SearchLocation
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("forecast.json")
    suspend fun getForecast(
        @Query("key") key: String = BuildConfig.WEATHERAPI_KEY,
        @Query("q") latLng: String,
        @Query("days") days: String = "7"
    ): Forecast

    @GET("search.json")
    suspend fun getCities(
        @Query("key") key: String = BuildConfig.WEATHERAPI_KEY,
        @Query("q") prefix: String,
    ): List<SearchLocation>
}