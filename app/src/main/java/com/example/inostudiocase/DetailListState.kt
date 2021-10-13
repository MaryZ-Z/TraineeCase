package com.example.inostudiocase

sealed class DetailListState {
    object Loading : DetailListState()

    data class Loaded(val movie: Movie) : DetailListState()

    data class Error(val message: String) : DetailListState()
}