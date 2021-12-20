package com.example.inostudiocase.common

sealed class DetailState<out T> {
    object Loading : DetailState<Nothing>()

    data class Loaded<T>(val data: T) : DetailState<T>()

    data class Error(val message: String) : DetailState<Nothing>()
}