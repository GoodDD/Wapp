package com.example.wapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.wapp.data.models.City


@Database(entities = [City::class], version = 1, exportSchema = false)
abstract class CityDb : RoomDatabase() {
    abstract fun cityDao(): CityDao
}