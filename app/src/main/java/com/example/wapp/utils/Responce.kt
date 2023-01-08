package com.example.wapp.utils

import com.example.wapp.data.models.Forecast
import kotlinx.serialization.Serializable

@Serializable
sealed interface Response<out T> {

    /*@Serializable
    object Loading: Response<out T>*/

    @Serializable
    data class Success<out T>(val data: T): Response<T>

    @Serializable
    data class Error(val message: String): Response<Nothing>

    val Response<*>.succeeded
        get() = this is Response.Success
}