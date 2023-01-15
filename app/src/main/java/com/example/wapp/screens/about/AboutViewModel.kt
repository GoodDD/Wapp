package com.example.wapp.screens.about

import androidx.compose.ui.platform.AndroidUriHandler
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AboutViewModel @Inject constructor(): ViewModel() {

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()

    fun onDialogOpen() {
        _showDialog.value = true
    }

    fun onDialogConfirm(uriHandler: UriHandler, url: String) {
        uriHandler.openUri(url)
        _showDialog.value = false
    }

    fun onDialogDismiss() {
        _showDialog.value = false
    }
}