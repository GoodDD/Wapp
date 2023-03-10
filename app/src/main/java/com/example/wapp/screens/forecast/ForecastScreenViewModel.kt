package com.example.wapp.screens.forecast

import android.net.Uri
import android.util.Log
import androidx.compose.ui.focus.FocusManager
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UriUtil
import com.example.wapp.R
import com.example.wapp.data.models.Forecast
import com.example.wapp.data.models.Forecastday
import com.example.wapp.data.models.Hour
import com.example.wapp.data.models.SearchLocation
import com.example.wapp.repository.WeatherRepository
import com.example.wapp.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

data class ForecastUiState(
    val searchText: String = "",
    val loading: Boolean = true,
    val forecast: Forecast? = null,
    val cities: List<SearchLocation> = emptyList(),
)

@OptIn(FlowPreview::class)
@HiltViewModel
class ForecastScreenViewModel @Inject constructor(
    private val repository: WeatherRepository,
    val player: Player
): ViewModel() {
    // TODO: TEMP, testing pull-to-refresh
    private val _searchText = MutableStateFlow("")

    private val _isLoading = MutableStateFlow(true)
    private val _forecast: MutableStateFlow<Forecast?> = MutableStateFlow(null)
    private val _cities = MutableStateFlow<List<SearchLocation>>(emptyList())

    val forecastUiState = combine(_searchText, _isLoading, _forecast, _cities) {
        searchText, isLoading, forecast, cities ->

            if (searchText.length <= 2 ) _cities.value = emptyList()
            //if (searchText.isBlank() || searchText.length < 2) clearSearch()

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
                    Log.i("GetCities", response.message)
                }
            }
        }
    }

    // TODO: Proper func name
    fun load(prefix: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            when (val response = repository.getForecast(prefix)) {
                is Response.Success -> {
                    _forecast.value = response.data
                    _searchText.value = response.data.location.name
                }
                is Response.Error -> {
                    Log.i(ForecastScreenViewModel::class.simpleName, response.message)
                }
            }
            _isLoading.value = false
        }
    }

    private fun getVideoUri(): Uri {
        val rawId = R.raw.clouds
        val videoUri = "android.resource://com.example.wapp/$rawId"
        return Uri.parse(videoUri)
    }

    init {
        additionalSearchTextSetup()
        player.apply {
            setMediaItem(MediaItem.fromUri(getVideoUri()))
            repeatMode = Player.REPEAT_MODE_ALL
            playWhenReady = true
        }.prepare()
    }

    fun formattedHours(days: List<Forecastday>): List<Hour> {
        val range = Instant.now().epochSecond.toInt()..Instant.now().epochSecond.toInt() + 86400
        val hours: MutableList<Hour> = (days[0].hour + days[1].hour) as MutableList<Hour>

        val formattedHours = mutableListOf<Hour>()
        hours.forEach { hour -> if (hour.time_epoch in range) { formattedHours.add(hour) } }

        return formattedHours
    }

    fun formattedDays(days: List<Forecastday>): List<Forecastday> {
        return days.slice(1 until days.size)
    }
}