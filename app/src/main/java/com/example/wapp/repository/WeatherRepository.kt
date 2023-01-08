package com.example.wapp.repository

import com.example.wapp.data.models.Forecast
import com.example.wapp.data.models.SearchLocation
import com.example.wapp.data.remote.WeatherApi
import com.example.wapp.utils.Response
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class WeatherRepository @Inject constructor(
    private val api: WeatherApi
) {
    suspend fun getForecast(latLng: String): Response<Forecast> {
        val response = try {
            api.getForecast(latLng = latLng)
        } catch (e: Exception) {
            return Response.Error(e.message!!)
        }
        return Response.Success(response)
    }

    suspend fun getCities(prefix: String): Response<List<SearchLocation>> {
        val response = try {
            api.getCities(prefix = prefix)
        } catch (e: Exception) {
            return Response.Error(e.message!!)
        }
        return Response.Success(response)
    }
}