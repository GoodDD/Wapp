package com.example.wapp.screens.forecast

import android.util.Log
import com.example.wapp.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// TODO: Spacer may be replaced?

@Composable
fun ForecastScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar()
        ForecastCurrent()
        //Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier
                //.fillMaxSize()
                .background(Color.LightGray)
                //.weight(1f)
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ForecastHourlySection()
                ForecastDailySection()
            }
        }
//        Row(
//            modifier = Modifier
//                //.fillMaxSize()
//                .background(Color.Cyan)
//                //.weight(1f)
//        ) {
//            ForecastDailySection()
//        }
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
fun ForecastCurrent() {
    Row(
        modifier = Modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Tallinn",
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                style = MaterialTheme.typography.displaySmall
            )
            Row {
                Text(
                    text = "10",
                    fontFamily = FontFamily.Monospace,
                    style = MaterialTheme.typography.displayLarge

                )
                Text(
                    text = "°C",
                    modifier = Modifier.padding(top = 8.dp),
                    fontFamily = FontFamily.Monospace,
                    style = MaterialTheme.typography.headlineLarge
                )
            }

            Text(
                text = "8/12°C",
                fontFamily = FontFamily.Monospace,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "Sunny",
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
fun ForecastHourlyCard(day: TempDay) {
    ElevatedCard(
        //modifier = Modifier.padding(vertical = 8.dp),
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = day.time,
                fontFamily = FontFamily.Monospace,
                style = MaterialTheme.typography.titleLarge
            )
            Image(
                painter = painterResource(R.drawable.sunny),
                contentDescription = "Sunny",
                modifier = Modifier.size(width = 48.dp, height = 48.dp)
            )
            Text(
                text = day.temp,
                fontFamily = FontFamily.Monospace,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Composable
fun ForecastDailyCard() {
    ElevatedCard(
        //modifier = Modifier.padding(vertical = 8.dp),
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Tomorrow",
                fontFamily = FontFamily.Monospace,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "28/12",
                fontFamily = FontFamily.Monospace,
                style = MaterialTheme.typography.titleLarge
            )
            Image(
                painter = painterResource(R.drawable.sunny),
                contentDescription = "Sunny",
                modifier = Modifier.size(width = 48.dp, height = 48.dp)
            )
            Text(
                text = "Sunny",
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
fun ForecastHourlySection() {

    val list = List(10) {
        TempDay(
            "12.00",
            "test",
            "12°C"
        )
    }

    LazyRow(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.DarkGray),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(items = list) { day ->
            ForecastHourlyCard(day)
        }
    }
}

@Composable
fun ForecastDailySection() {

    //val range = 0..10
    val list = List(10) {
        it
    }
    for(i in list) {
        Log.i("Daily List", i.toString())
    }

    LazyRow(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.DarkGray),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(items = list) {
            ForecastDailyCard()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ForecastDailySectionPreview() {
    ForecastDailySection()
}

@Preview(showBackground = true)
@Composable
fun ForecastHourlySectionPreview() {
    ForecastHourlySection()
}

@Preview(showBackground = true)
@Composable
fun ForecastCurrentPreview() {
    ForecastCurrent()
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