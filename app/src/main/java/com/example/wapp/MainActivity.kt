package com.example.wapp

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavGraph
import com.example.wapp.screens.NavGraphs
import com.example.wapp.screens.destinations.ForecastScreenDestination
import com.example.wapp.screens.forecast.ForecastScreen
import com.example.wapp.screens.location.LocationScreen
import com.example.wapp.ui.theme.WappTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var keepSplashOpened = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition { keepSplashOpened }
        setContent {
            WappTheme {
                // A surface container using the 'background' color from the theme
                //ForecastScreen()
                ForecastScreen(onDataLoaded = { keepSplashOpened = false })
                //DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }
}