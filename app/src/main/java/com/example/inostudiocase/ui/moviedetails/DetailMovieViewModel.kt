package com.example.inostudiocase.ui.moviedetails

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inostudiocase.common.DetailState
import com.example.inostudiocase.common.Screen
import com.example.inostudiocase.data.Movie
import com.example.inostudiocase.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailMovieViewModel @Inject constructor(
    private val repository: MovieRepository,
    private val savedState: SavedStateHandle
) : ViewModel() {
    private val movieId = savedState.get<Int>(Screen.DetailMovieScreen.MOVIE_ID)?: 0
    val state = mutableStateOf<DetailState<Movie>>(DetailState.Loading)

    init {
        movie()
        viewModelScope.launch {
            repository.flow.collect {
                val curState = state.value
                if (curState is DetailState.Loaded) {
                    state.value = checkLikes(curState.data)
                }
            }
        }
    }

    fun movie() {
        state.value = DetailState.Loading
        viewModelScope.launch {
            try {
                val details = repository.movie(movieId)
                state.value = checkLikes(details)
            } catch (e: Exception) {
                state.value = DetailState.Error(e.message.toString())
            }
        }
    }

    fun onLikeClick(movie: Movie) {
        viewModelScope.launch {
            repository.onLikeClick(movie)
        }
    }

    private fun checkLikes(movies: Movie): DetailState.Loaded<Movie> {
        val newMovie = movies.copy(isLiked = repository.isLiked(movies.id))
        return DetailState.Loaded(newMovie)
    }
}