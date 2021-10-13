package com.example.inostudiocase

sealed class ListState {
    object Loading : ListState()

    data class Loaded(val movies: List<Movie>) : ListState()

    object Empty : ListState()

    data class Error(val message: String) : ListState()
    //object Error : ListState()
}