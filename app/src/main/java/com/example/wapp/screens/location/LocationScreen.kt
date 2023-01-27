package com.example.wapp.screens.location

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.wapp.data.models.City
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Destination
@Composable
fun LocationScreen(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator,
    viewModel: LocationViewModel = hiltViewModel()
) {

    val cityName by viewModel.searchText.collectAsState("")

    val cities by viewModel.cities.collectAsState(emptyList())

    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(navigator = navigator) }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Column(
                modifier = modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                OutlinedTextField(
                    value = cityName,
                    onValueChange = viewModel::onCityNameSearch,
                    modifier = modifier
                        .fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = {
                            viewModel.addCity(cityName)
                        }) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            viewModel.addCity(cityName)
                        }
                    )
                )

                LazyColumn(
                    modifier = modifier.fillMaxWidth()
                ) {
                    items(cities) { city: City ->
                        CityRow(modifier, city)
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    navigator: DestinationsNavigator
) {

    TopAppBar(
        title = { Text("Locations") },
        navigationIcon = {
            IconButton(onClick = { navigator.popBackStack() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }
        }
    )
}

@Composable
fun CityRow(
    modifier: Modifier,
    city: City
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Green)
    ) {
        Text(text = city.title)
    }
}