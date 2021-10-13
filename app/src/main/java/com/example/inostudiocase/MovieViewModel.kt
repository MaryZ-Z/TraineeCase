package com.example.inostudiocase

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val repository: MovieRepository) :
    ViewModel() {

    val state = mutableStateOf<ListState>(ListState.Loading)
    val query = mutableStateOf("")
    val isRefreshing = mutableStateOf(false)
    val showError = MutableStateFlow<String?>(null)
    private var searchJob: Job? = null
    private var likes: List<Int>? = null

    init {
        fetchMovies()
    }

    fun search(query: String) {
        this.query.value = query
        refresh()
    }

    private fun search() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            try {
                val items = repository.search(query.value)
                state.value = if (items.isEmpty()) ListState.Empty else checkLikes(items)
            } catch (e: Exception) {
                showError(e.message.toString())
            }
            isRefreshing.value = false
        }
    }

    fun onLikeClick(movie: Movie) {
        viewModelScope.launch {
            if (likes?.contains(movie.id) == true) {
                repository.delete(movie)
            } else {
                repository.insert(movie)
            }
            val currentState = state.value
            if (currentState is ListState.Loaded) state.value = checkLikes(currentState.movies)
        }
    }

    fun refresh() {
        isRefreshing.value = true
        if (query.value.isEmpty()) fetchMovies() else search()
    }

    private fun fetchMovies() {
        viewModelScope.launch {
            try {
                val items = repository.discover()
                state.value = checkLikes(items)
            } catch (e: Exception) {
                showError(e.message.toString())
            }
            isRefreshing.value = false
        }
    }

    private suspend fun checkLikes(items: List<Movie>): ListState {
        likes = repository.getLikes().map { it.id }
        val movies = items.toMutableList().apply {
            forEachIndexed { index, movie ->
                this[index] = this[index].copy(isLiked = likes?.contains(movie.id) == true)
            }
        }
        return ListState.Loaded(movies)
    }

    private fun showError(message: String) {
        if (state.value !is ListState.Loaded) state.value = ListState.Error(message)
        viewModelScope.launch { showError.emit(message) }
    }
}