package com.example.wapp.data.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "city_table", indices = [Index(value = ["title"], unique = true)])
data class City(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String
)