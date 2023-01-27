package com.example.wapp.repository

import com.example.wapp.data.models.City
import com.example.wapp.database.CityDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CityRepository @Inject constructor(
    private val cityDao: CityDao
){

    fun getCities(): Flow<List<City>> {
        return cityDao.getCities()
    }

    suspend fun addCity(city: City) {
        cityDao.addCity(city)
    }
}