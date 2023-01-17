package com.example.wapp.screens.about

import android.util.Log
import com.example.wapp.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun AboutScreen(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator,
    viewModel: AboutViewModel = hiltViewModel()
) {

    val showDialogState by viewModel.showDialog.collectAsState()

    val annotatedString = buildAnnotatedString {
        append("Icons made by ")

        pushStringAnnotation(tag = "URL", annotation = "https://www.flaticon.com/authors/freepik")
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
            append("Freepik ")
        }
        pop()

        append("from ")

        pushStringAnnotation(tag = "URL", annotation = "https://www.flaticon.com/")
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
            append("www.flaticon.com ")
        }
        pop()

        append("and ")

        pushStringAnnotation(tag = "URL", annotation = "https://fonts.google.com/icons")
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
            append("Material Icons")
        }
        pop()
    }

    val uriHandler = LocalUriHandler.current
    var linkUrl = ""

    Scaffold(
        modifier = modifier.padding(16.dp),
        topBar = { TopAppBar(navigator) }
    ) { paddingValues ->

        RedirectDialog(
            url = linkUrl,
            show = showDialogState,
            onDismiss = viewModel::onDialogDismiss,
            onConfirm = { viewModel.onDialogConfirm(uriHandler, linkUrl) }
        )

        Column(
            modifier = modifier.padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Project created by Dmitri Pantsenko",
                style = MaterialTheme.typography.titleLarge
            )
            ClickableText(
                text = annotatedString,
                style = MaterialTheme.typography.titleLarge + TextStyle(textAlign = TextAlign.Center),
                onClick = { offset ->
                    annotatedString.getStringAnnotations(tag = "URL", start = offset, end = offset).firstOrNull()?.let { url ->
                        linkUrl = url.item
                        viewModel.onDialogOpen()
                    }
                }
            )
            Spacer(modifier = modifier.weight(1f))
            Text(
                text = "Powered by WeatherAPI.com",
                modifier = modifier,
                style = MaterialTheme.typography.titleLarge
            )
            Image(
                painter = painterResource(id = R.drawable.weatherapi_logo),
                contentDescription = "Weather api logo",
                modifier = modifier.size(width = 128.dp, height = 64.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    navigator: DestinationsNavigator
) {
    
    TopAppBar(
        title = { Text("About") },
        navigationIcon = {
            IconButton(onClick = { navigator.popBackStack() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }
        }
    )
}

@Composable
fun RedirectDialog(
    url: String,
    show: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    if (show) {
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = { onConfirm() } ) {
                    Text(text = "OK")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(text = "Cancel")
                }
            },
            title = { Text(text = "Redirect") },
            text = { Text(text = "You will be redirected to $url") }
        )
    }
}

@Preview
@Composable
fun AboutScreenPreview() {
    AboutScreen(navigator = EmptyDestinationsNavigator)
}

@Preview
@Composable
fun RedirectDialogPreview() {
    //RedirectDialog("https://www.flaticon.com/authors/freepik")
}