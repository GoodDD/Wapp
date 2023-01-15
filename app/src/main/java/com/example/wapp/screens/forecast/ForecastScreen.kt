package com.example.wapp.screens.forecast

import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.ui.AspectRatioFrameLayout.RESIZE_MODE_ZOOM
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.wapp.R
import com.example.wapp.data.models.Current
import com.example.wapp.data.models.Forecast
import com.example.wapp.data.models.Forecastday
import com.example.wapp.data.models.Hour

// TODO: Think about pull-to-refresh

@OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalLifecycleComposeApi::class
)
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
fun ForecastScreen(
    viewModel: ForecastScreenViewModel = hiltViewModel(),
) {

    val uiState = viewModel.forecastUiState.collectAsStateWithLifecycle().value

    val pullRefreshState = rememberPullRefreshState(uiState.loading, { viewModel.load(uiState.searchText) })

    // TODO: Rewrite background video part
    DisposableEffect(
        AndroidView(
            factory = { context ->  
                PlayerView(context).apply {
                    player = viewModel.player
                    layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                    useController = false
                    resizeMode = RESIZE_MODE_ZOOM
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    ) {
        onDispose {
            viewModel.player.release()
        }
    }

    Box(
        Modifier
            .pullRefresh(pullRefreshState)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (uiState.loading) {
                CircularProgressIndicator()
            } else {
                uiState.forecast?.let { forecast ->
                    ForecastWrapper(forecast)
                }
            }
        }
        //PullRefreshIndicator(uiState.loading, pullRefreshState, Modifier.align(Alignment.TopCenter))
    }
}

@Composable
fun ForecastWrapper(forecast: Forecast) {
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ForecastCurrent(forecast)
        ForecastHourlySection(forecast.forecast.forecastday[0].hour)
        ForecastDailySection(forecast.forecast.forecastday)
        ExtrasDataSection(forecast.current)
    }
}

@Composable
fun ForecastCurrent(forecast: Forecast) {

    Row(
        modifier = Modifier.padding(top = 16.dp, bottom = 32.dp),
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
            Text(
                text = forecast.current.condition.text,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}

@Composable
fun ForecastHourlyCard(hour: Hour) {
    ElevatedCard {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = hour.time.takeLast(5),
            )
            AsyncImage(
                modifier = Modifier.size(36.dp),
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https:${hour.condition.icon}")
                    .crossfade(true)
                    .build(),
                contentDescription = hour.condition.text
            )
            Text(
                text = hour.temp_c.toString()+"°C",
            )
        }
    }
}

@Composable
fun ForecastHourlySection(hours: List<Hour>) {
    
    LazyRow(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(items = hours) { hour: Hour ->
            ForecastHourlyCard(hour)
        }
    }
}

@Composable
fun ForecastDailyCardExperimental(forecast: Forecastday) {

    var expanded by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f
    )

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            )
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    modifier = Modifier.size(36.dp),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("https:${forecast.day.condition.icon}")
                        .crossfade(true)
                        .build(),
                    contentDescription = forecast.day.condition.text
                )

                Row(
                    modifier = Modifier
                        .weight(1f),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = forecast.day.avgtemp_c.toString())
                    Text(text = "°C")
                }
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(3f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = forecast.day.condition.text,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = forecast.date.takeLast(5))
                        Text(text = "${forecast.day.maxtemp_c}/${forecast.day.mintemp_c}°C")
                    }
                }
                IconButton(
                    onClick = {
                              expanded = !expanded
                    },
                    modifier = Modifier
                        .rotate(rotation)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Drop-Down Arrow",
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
            
            if (expanded) {
                Divider(
                    modifier = Modifier.padding(8.dp),
                    thickness = 1.dp
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    ExtrasItemCard(
                        R.drawable.drop,
                        "Drop",
                        "Wind Speed",
                        forecast.day.maxwind_kph.toString()
                    )
                    ExtrasItemCard(
                        R.drawable.drop,
                        "Drop",
                        "Humidity",
                        forecast.day.avghumidity.toString()
                    )
                    ExtrasItemCard(
                        R.drawable.drop,
                        "Drop",
                        "UV",
                        forecast.day.uv.toString()
                    )
                }
            }
        }
    }
}

@Composable
fun ForecastDailySection(days: List<Forecastday>) {

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f))
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        days.forEach { day : Forecastday ->
            ForecastDailyCardExperimental(forecast = day)
        }
    }
}

@Composable
fun ExtrasDataSection(current: Current) {

    Row(
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f))
            .padding(32.dp),
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ExtrasItemCard(
                R.drawable.wind,
                "Wind",
                "Wind Speed",
                current.wind_kph.toString()
            )
            ExtrasItemCard(
                R.drawable.drop,
                "Drop",
                "Humidity",
                current.humidity.toString()
            )
        }
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ExtrasItemCard(
                R.drawable.thermometer,
                "Thermometer",
                "Feels like",
                current.feelslike_c.toString()
            )
            ExtrasItemCard(
                R.drawable.ultraviolet,
                "UV",
                "UV",
                current.uv.toString()
            )
        }
    }
}

@Composable
fun ExtrasItemCard(
    iconId: Int,
    contentDescription: String,
    title: String,
    value: String
) {

    Column(
        horizontalAlignment =  Alignment.CenterHorizontally
    ) {
        Icon(
            painterResource(id = iconId),
            contentDescription = contentDescription,
            modifier = Modifier.size(24.dp)
        )
        Text(text = title)
        Text(text = value)
    }
}