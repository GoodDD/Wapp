package com.example.wapp.screens.location

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun LocationScreen(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator,
) {
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
                    value = "Kappa",
                    onValueChange = { },
                    modifier = modifier
                        .fillMaxWidth(),
                    trailingIcon = { IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                    }}
                )
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