package com.example.wapp.screens.forecast

import android.util.Log
import com.example.wapp.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.pullrefresh.*
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.wapp.data.models.Forecast
import com.example.wapp.data.models.Forecastday
import com.example.wapp.data.models.Hour
import com.example.wapp.data.models.SearchLocation

// TODO: Spacer may be replaced?

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class,
    ExperimentalLifecycleComposeApi::class
)
@Composable
fun ForecastScreen(
    viewModel: ForecastScreenViewModel = hiltViewModel(),
) {

    val uiState = viewModel.forecastUiState.collectAsStateWithLifecycle().value

    //val isLoading = viewModel.isLoading.collectAsState()
    val pullRefreshState = rememberPullRefreshState(uiState.loading, { viewModel.load(uiState.searchText) })

//    LaunchedEffect(uiState.forecast) {
//        viewModel.load()
//        Log.i("12345", "launch")
//    }
    Log.i("322", uiState.loading.toString())
    uiState.forecast?.location?.let {
        Log.i("123", it.localtime)
        Log.i("forecastui", it.toString())
    }
//    when (uiState.forecast) {
//        is Response.Success -> {
//            Log.i("321", uiState.forecast.data.location.localtime)
//        }
//        is Response.Error -> {
//
//        }
//        is Response.Loading -> {
//
//        }
//        else -> {}
//    }
//    when(uiState.forecast) {
//        is Response.Success -> {
//            Log.i("123", uiState.forecast.data.location.localtime)
//        }
//        is Response.Error -> {
//            Log.i("123", "error")
//        }
//        is Response.Loading -> {
//            Log.i("123", "loading")
//        }
//    }
//    val forecast = produceState<Response>(initialValue = Response.Loading) {
//        value = viewModel.getForecast()
//        Log.i("Produce", "1")
//    }.value
//
//    when(forecast) {
//        is Response.Success -> {
//            Log.i("ForecastScreen", forecast.data.location.localtime)
//        }
//        is Response.Error -> {
//            Log.i("ForecastScreen", forecast.message)
//        }
//        is Response.Loading -> {
//            Log.i("ForecastScreen", "Loading...")
//        }
//    }
    val focusManager = LocalFocusManager.current
    // TODO: TEMP ExposedDropdown test
    //var expanded by remember { mutableStateOf(false) }
    //if (uiState.cities.isNotEmpty()) expanded = true
    //var selectedOptionText by remember { mutableStateOf(options[0]) }
    Log.i("Cities", uiState.cities.toString())
    //Log.i("Expanded", expanded.toString())
    Box(
        Modifier
            .pullRefresh(pullRefreshState)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        viewModel.clearSearch()
                        focusManager.clearFocus()
                    })
                },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (uiState.loading) {
                CircularProgressIndicator()
            } else {
                CitySearchField(
                    prefix = uiState.searchText,
                    onPrefixChanged = viewModel::onCityNameSearch,
                    cities = uiState.cities,
                    onCitySelected = viewModel::onCityNameSelected,
                    getCities = viewModel::getCities,
                    focusManager = focusManager,
                    clear = viewModel::clearSearch
                )
                uiState.forecast?.let { ForecastCurrent(it) }
            }
//            TopAppBar()
//            ForecastCurrent(forecast.data)
//            // TODO: Replace place row in a section
//            Row(
//                modifier = Modifier.background(Color.LightGray)
//            ) {
//                Column(
//                    modifier = Modifier.padding(8.dp),
//                    verticalArrangement = Arrangement.spacedBy(8.dp)
//                ) {
//                    ForecastHourlySection(forecast.data)
//                    ForecastDailySection(forecast.data)
//                }
//            }
        }
        PullRefreshIndicator(uiState.loading, pullRefreshState, Modifier.align(Alignment.TopCenter))
    }

//    when(forecast) {
//        is Response.Success -> {
//            Box(
//                Modifier
//                    .pullRefresh(pullRefreshState)
//            ) {
//                Column(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .verticalScroll(rememberScrollState()),
//                    verticalArrangement = Arrangement.SpaceBetween,
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    TopAppBar()
//                    ForecastCurrent(forecast.data)
//                    // TODO: Replace place row in a section
//                    Row(
//                        modifier = Modifier.background(Color.LightGray)
//                    ) {
//                        Column(
//                            modifier = Modifier.padding(8.dp),
//                            verticalArrangement = Arrangement.spacedBy(8.dp)
//                        ) {
//                            ForecastHourlySection(forecast.data)
//                            ForecastDailySection(forecast.data)
//                        }
//                    }
//                }
//                PullRefreshIndicator(isLoading.value, pullRefreshState, Modifier.align(Alignment.TopCenter))
//            }
//        }
//        is Response.Error -> {
//
//        }
//        is Response.Loading -> {
//
//        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitySearchField(
    prefix: String,
    onPrefixChanged: (String) -> Unit,
    cities: List<SearchLocation>,
    onCitySelected: (String, FocusManager) -> Unit,
    getCities: (String) -> Unit,
    focusManager: FocusManager,
    clear: () -> Unit // TODO: MB remove?
) {
    var expanded by remember { mutableStateOf(false) }
    expanded = cities.isNotEmpty() // TODO: Watch this part over

    Log.i("CitiesInTest", cities.toString())
    Log.i("Expanded", expanded.toString())
    Log.i("Length", prefix.length.toString())
    Log.i("Prefix", prefix)

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { _ ->
            //getCities(prefix) // TODO: Prefix is not seen inside
        }

    ) {
        TextField(
            value = prefix,
            onValueChange = {
                onPrefixChanged(it)
                //expanded = cities.isNotEmpty()
            },
            modifier = Modifier.menuAnchor(),
            readOnly = false,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = { // TODO: Optimize this! autocomplete textfield with first match in citites
                    onCitySelected(prefix, focusManager)
                }
            )
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                Log.i("Dismiss?", "Yep")
                //clear()

                //viewModel.clearSearch()
                //expanded = false
                //focusManager.clearFocus()
            }
        ) {
            cities.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(text = selectionOption.name) },
                    onClick = {
                        //Log.i("CityClicked", selectionOption.name)
                        //onPrefixChanged(selectionOption.name)
                        onCitySelected(selectionOption.name, focusManager)
                        //prefix = selectionOption.name
                        //viewModel.onCityNameSelected(selectionOption.name)
                        //viewModel.clearSearch()
                        //focusManager.clearFocus()
                    }
                )
            }
        }
    }
}

@Composable
fun TopAppBar() {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
    ) {
        Icon(
            modifier = Modifier.size(48.dp),
            imageVector = Icons.Filled.ChevronLeft,
            contentDescription = "Back"
        )
    }
}

@Composable
fun ForecastCurrent(forecast: Forecast) {

    Row(
        modifier = Modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = forecast.location.name,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                style = MaterialTheme.typography.displaySmall
            )
            Row {
                Text(
                    text = forecast.current.temp_c.toString(),
                    style = MaterialTheme.typography.displayLarge

                )
                Text(
                    text = "°C",
                    modifier = Modifier.padding(top = 8.dp),
                    style = MaterialTheme.typography.headlineLarge
                )
            }

//            Text(
//                text = "8/12°C",
//                fontFamily = FontFamily.Monospace,
//                style = MaterialTheme.typography.titleLarge
//            )
            Text(
                text = forecast.current.condition.text,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}

// TODO: TEMP
@Composable
fun ForecastHourlyCard() {
    ElevatedCard(modifier = Modifier) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "13.00",
                fontFamily = FontFamily.Monospace,
                style = MaterialTheme.typography.titleLarge
            )
            Image(
                painter = painterResource(R.drawable.sunny),
                contentDescription = "Sunny",
                modifier = Modifier.size(width = 48.dp, height = 48.dp)
            )
            Text(
                text = "12°C",
                fontFamily = FontFamily.Monospace,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Composable
fun ForecastHourlyCard(hour: Hour) {
    ElevatedCard(
        //modifier = Modifier.padding(vertical = 8.dp),
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = hour.time.takeLast(5),
                fontFamily = FontFamily.Monospace,
                style = MaterialTheme.typography.titleLarge
            )
            AsyncImage(
                modifier = Modifier.size(width = 48.dp, height = 48.dp),
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https:${hour.condition.icon}")
                    .crossfade(true)
                    .build(),
                contentDescription = hour.condition.text
            )
            Text(
                text = hour.temp_c.toString()+"°C",
                fontFamily = FontFamily.Monospace,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Composable
fun ForecastDailyCard(day: Forecastday) {
    ElevatedCard(
        modifier = Modifier
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // TODO: Adaptive day of the week
            Text(
                text = "Tomorrow",
                fontFamily = FontFamily.Monospace,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = day.date.takeLast(5),
                fontFamily = FontFamily.Monospace,
                style = MaterialTheme.typography.titleLarge
            )
            AsyncImage(
                modifier = Modifier.size(width = 48.dp, height = 48.dp),
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https:${day.day.condition.icon}")
                    .crossfade(true)
                    .build(),
                contentDescription = day.day.condition.text
            )
            Text(
                modifier = Modifier.width(128.dp),
                text = day.day.condition.text,
                fontFamily = FontFamily.Monospace,
                overflow = TextOverflow.Ellipsis,
                softWrap = true,
                maxLines = 1,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = day.day.avgtemp_c.toString()+"°C",
                fontFamily = FontFamily.Monospace,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

data class TempDay(
    val time: String,
    val icon: String,
    val temp: String
)
// TODO: TEMP
@Composable
fun ForecastHourlySection(forecast: Forecast) {

    val list = List(10) {
        TempDay(
            "12.00",
            "test",
            "12°C"
        )
    }

    val hours: List<Hour> = forecast.forecast!!.forecastday[0].hour

    LazyRow(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.DarkGray),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(items = hours) { hour: Hour ->
            ForecastHourlyCard(hour)
        }
    }
}

@Composable
fun ForecastDailySection(forecast: Forecast) {

    val days = forecast.forecast!!.forecastday

    LazyRow(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.DarkGray),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(items = days) { day: Forecastday ->
            ForecastDailyCard(day)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitySearchField() {

    var text by remember { mutableStateOf("Hallo") }

//    OutlinedTextField(
//        value = search,
//        onValueChange = { text = it },
//        label = { Text("City") }
//    )
}

@Preview(
    showBackground = true,
)
@Composable
fun CitySearchFieldPreview() {
    CitySearchField()
}

@Preview(showBackground = true)
@Composable
fun ForecastDailySectionPreview() {
    //ForecastDailySection()
}

@Preview(showBackground = true)
@Composable
fun ForecastHourlySectionPreview() {
    //ForecastHourlySection()
}

@Preview(showBackground = true)
@Composable
fun ForecastCurrentPreview() {
    //ForecastCurrent()
}

@Preview
@Composable
fun ForecastHourCardPreview() {
    ForecastHourlyCard()
}

@Preview(
    showSystemUi = true
)
@Composable
fun ForecastScreenPreview() {
    ForecastScreen()
}