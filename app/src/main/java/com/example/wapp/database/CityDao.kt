package com.example.wapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.wapp.data.models.City
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {

    @Query("SELECT * FROM city_table")
    fun getCities(): Flow<List<City>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCity(city: City)
}