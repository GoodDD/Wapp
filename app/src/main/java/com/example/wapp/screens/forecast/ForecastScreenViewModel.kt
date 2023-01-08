package com.example.wapp.screens.forecast

import android.util.Log
import androidx.compose.ui.focus.FocusManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wapp.data.models.Forecast
import com.example.wapp.data.models.ForecastX
import com.example.wapp.data.models.SearchLocation
import com.example.wapp.repository.WeatherRepository
import com.example.wapp.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.http.Query
import javax.inject.Inject


data class ForecastUiState(
    val searchText: String = "",
    val loading: Boolean = true,
    val forecast: Forecast? = null,
    val cities: List<SearchLocation> = emptyList()
)

@OptIn(FlowPreview::class)
@HiltViewModel
class ForecastScreenViewModel @Inject constructor(
    private val repository: WeatherRepository
): ViewModel() {

    // TODO: TEMP, testing pull-to-refresh
    private var _searchText = MutableStateFlow("")
    private val _isLoading = MutableStateFlow(true)
    private val _forecast: MutableStateFlow<Forecast?> = MutableStateFlow(null)
    private val _cities = MutableStateFlow<List<SearchLocation>>(emptyList())

    val forecastUiState = combine(_searchText, _isLoading, _forecast, _cities) {
        searchText, isLoading, forecast, cities ->

            if (searchText.isBlank() || searchText.length < 3) clearSearch()

            ForecastUiState(
                searchText,
                isLoading,
                forecast,
                cities

            )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ForecastUiState()
    )

    fun onCityNameSearch(prefix: String) {
        _searchText.value = prefix
        Log.i(ForecastScreenViewModel::class.simpleName, prefix)
    }

    fun onCityNameSelected(prefix: String, focusManager: FocusManager) {
        _searchText.value = prefix
        load(prefix)
        clearSearch()
        focusManager.clearFocus()
    }

    fun clearSearch() {
            _cities.value = emptyList()
            Log.i(ForecastScreenViewModel::class.simpleName, "Cleared! $_cities")

    }

    private fun additionalSearchTextSetup() {
        _searchText
            .debounce(300) // gets the latest; no need for delays!
            .filter { cityPrefix -> (cityPrefix.isNotEmpty()
                    && cityPrefix.length > 2) } // don't call if 1 or empty
            .distinctUntilChanged() // to avoid duplicate network calls
            .flowOn(Dispatchers.IO) // Changes the context where this flow is executed to Dispatchers.IO
            .onEach { cityPrefix -> // just gets the prefix: 'ph', 'pho', 'phoe'
                Log.i(ForecastScreenViewModel::class.simpleName, cityPrefix)
                getCities(cityPrefix)
            }
            .launchIn(viewModelScope)
    }

    fun getCities(prefix: String) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i("GetCitiesData", prefix)
            when (val response = repository.getCities(prefix)) {

                is Response.Success -> {
                    _cities.value = response.data
                    Log.i("GetCities", response.data.toString())
                }
                is Response.Error -> {
                    Log.i("getCities", response.message)
                }
            }
        }
    }

    // TODO: Proper func name
    fun load(prefix: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            _forecast.value = null
            when (val response = repository.getForecast(prefix)) {
                is Response.Success -> {
                    Log.i("Won", "responce success")
                    _forecast.value = response.data
                }
                is Response.Error -> {
                    Log.i("wtfE", "error")
                }
                /*is Response.Loading -> {
                    Log.i("wtfL", "loading")
                }*/
            }
            //getForecast()
            _isLoading.value = false
            //Log.i(ForecastScreenViewModel::class.simpleName, cityPrefix)
        }
    }
    // TODO: TEMP

    // TODO: TESTING FEATURE
//    fun getForecastState() {
//        _forecast.value = null
//        viewModelScope.launch(Dispatchers.IO) {
//            when (val response = repository.getForecast("59.45,24.75")) {
//                is Response.Success -> {
//                    _forecast.value = response.data
//                    _isLoading.value = false
//                    Log.i(ForecastScreenViewModel::class.simpleName, _forecast.value!!.location.localtime.toString())
//                }
//                is Response.Error -> {
//                    _isLoading.value = false
//                    Log.i(ForecastScreenViewModel::class.simpleName, "Error in getForecastState")
//                }
//                is Response.Loading -> {
//                    _isLoading.value = true
//                    Log.i(ForecastScreenViewModel::class.simpleName, "Loading in getForecastState")
//                }
//            }
//        }
//    }
     //

    init {
        additionalSearchTextSetup()
        load("Tallinn")
    }

//    suspend fun getForecast(): Response {
//        //return repository.getForecast("59.45,24.75")
//        return when (val response = repository.getForecast("59.45,24.75")) {
//
//            is Response.Success -> {
//                _forecast.value = Response.Loading
//                Log.i("wtf1", _forecast.value.toString())
//                _forecast.value = response
//                Log.i("wtf2", response.data.location.localtime)
//                response
//            }
//            is Response.Error -> {
//                _forecast.value = response
//                response
//            }
//            is Response.Loading -> {
//                _forecast.value = response
//                response
//            }
//        }
//    }

//    val test = combine() {
//
//    }.stateIn(
//        scope = viewModelScope,
//        started = SharingStarted.WhileSubscribed(),
//        initialValue = "123"
//    )

//    private var _searchText = MutableStateFlow("")
//    var searchText = _searchText.asStateFlow()
//    private val _cities = MutableStateFlow<List<SearchLocation>>(emptyList())
//
//    init {
//
//        _searchText
//            .debounce(300)
//            .filter { prefix ->
//                (prefix.isNotEmpty() && prefix.length > 2)
//            }
//            .distinctUntilChanged()
//            .flowOn(Dispatchers.IO)
//            .onEach { prefix ->
//                getCityNames(prefix)
//            }
//            .launchIn(viewModelScope)
//    }
//
//    fun onCityNameSearch(prefix: String) {
//        _searchText.value = prefix
//    }
//
//    private fun getCityNames(prefix: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            when(val response = repository.getCities(prefix)) {
//                is Response.Success<*> -> {
//                    _cities.value = response.data as List<SearchLocation>
//                }
//                is Response.Error -> {
//
//                }
//                else -> {}
//            }
//        }
//    }

//    fun onCityNameSearch(prefix: String) {
//        _searchText.value = prefix
//    }
}