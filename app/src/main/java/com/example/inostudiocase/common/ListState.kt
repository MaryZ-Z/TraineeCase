package com.example.inostudiocase.common

sealed class ListState<out T> {
    object Loading : ListState<Nothing>()

    data class Loaded<T>(val data: List<T>) : ListState<T>()

    object Empty : ListState<Nothing>()

    data class Error(val message: String) : ListState<Nothing>()
}