package com.example.wapp

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph
import com.example.wapp.screens.NavGraphs
import com.example.wapp.screens.forecast.ForecastScreen
import com.example.wapp.ui.theme.WappTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WappTheme {
                // A surface container using the 'background' color from the theme
                //ForecastScreen()
                DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }
}