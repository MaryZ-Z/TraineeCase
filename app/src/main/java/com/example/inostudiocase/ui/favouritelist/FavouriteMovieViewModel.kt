package com.example.inostudiocase.ui.favouritelist

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inostudiocase.common.ListState
import com.example.inostudiocase.data.Movie
import com.example.inostudiocase.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteMovieViewModel @Inject constructor(
    private val repository: MovieRepository,
) : ViewModel() {
    val state = mutableStateOf<ListState<Movie>>(ListState.Loading)

    init {
        viewModelScope.launch {
            repository.favourite().collect { movies ->
                state.value = if (movies.isEmpty()) ListState.Empty else ListState.Loaded(movies)
            }
        }
    }

    fun onLikeClick(movie: Movie) {
        viewModelScope.launch {
            repository.onLikeClick(movie)
        }
    }
}
