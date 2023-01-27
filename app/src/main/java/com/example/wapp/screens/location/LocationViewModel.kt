package com.example.wapp.screens.location

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.text.toUpperCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wapp.data.models.City
import com.example.wapp.repository.CityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val repository: CityRepository
): ViewModel() {

    val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText.asStateFlow()

    var _cities = MutableStateFlow<List<City>>(emptyList())

    val cities = repository.getCities()

//    fun cities() {
//        viewModelScope.launch {
//            _cities = repository.getCities()
//        }
//    }
    //val cities: StateFlow<List<City>> =

//    fun getCities() {
//        viewModelScope.launch(Dispatchers.IO) {
//            _cities = repository.getCities()
//            Log.i(LocationViewModel::class.simpleName, cities.value.toString())
//        }
//    }
    fun clear() {
        _searchText.value = ""
    }

    fun addCity(cityName: String) {

        val city = City(
            id = 0,
            title = if (cityName[0].isUpperCase()) {
                cityName
            } else {
                cityName.substring(0,1).uppercase() + cityName.substring(1)
            }
        )

        viewModelScope.launch {
            repository.addCity(city)
            clear()
        }
    }

    fun onCityNameSearch(cityName: String) {
        _searchText.value = cityName
    }

    init {
        //addCity(City(0, "Tallinn"))
        //getCities()
    }
}