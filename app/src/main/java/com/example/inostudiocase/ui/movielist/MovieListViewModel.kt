package com.example.inostudiocase.ui.movielist

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inostudiocase.common.ListState
import com.example.inostudiocase.data.Movie
import com.example.inostudiocase.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {
    val state = mutableStateOf<ListState<Movie>>(ListState.Loading)
    val query = mutableStateOf("")
    val isRefreshing = mutableStateOf(false)
    val showError = MutableStateFlow<String?>(null)
    private var searchJob: Job? = null

    init {
        discover()
        viewModelScope.launch {
            repository.flow.collect {
                val curState = state.value
                if (curState is ListState.Loaded) {
                    state.value = checkLikes(curState.data)
                }
            }
        }
    }

    fun search(query: String) {
        this.query.value = query
        onRefresh()
    }

    private fun search() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            try {
                val items = repository.search(query.value)
                state.value = if (items.isEmpty()) ListState.Empty else checkLikes(items)
            } catch (e: Exception) {
                if (e !is CancellationException) showError(e.message.toString())
            }
            isRefreshing.value = false
        }
    }

    fun onLikeClick(movie: Movie) {
        viewModelScope.launch {
            repository.onLikeClick(movie)
        }
    }

    fun onRefresh() {
        isRefreshing.value = true
        if (query.value.isEmpty()) discover() else search()
    }

    private fun discover() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            try {
                val items = repository.discover()
                state.value = checkLikes(items)
            } catch (e: Exception) {
                if (e !is CancellationException) showError(e.message.toString())
            }
            isRefreshing.value = false
        }
    }

    private fun checkLikes(movies: List<Movie>): ListState.Loaded<Movie> {
        val newMovies = movies.toMutableList().apply {
            replaceAll { movie ->
                movie.copy(isLiked = repository.isLiked(movie.id))
            }
        }
        return ListState.Loaded(newMovies)
    }

    private fun showError(message: String) {
        if (state.value !is ListState.Loaded) {
            state.value = ListState.Error(message)
        } else {
            viewModelScope.launch { showError.emit(message) }
        }
    }
}