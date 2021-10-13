package com.example.inostudiocase

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repository: MovieRepository):
    ViewModel(){
    val state = mutableStateOf<DetailListState>(DetailListState.Loading)

    fun movie(movieId: Int){
        state.value = DetailListState.Loading
        viewModelScope.launch {
            try {
                val details = repository.movie(movieId)
                state.value = DetailListState.Loaded(details)
            } catch(e: Exception) {
                state.value = DetailListState.Error(e.message.toString())
            }
        }
    }

    fun onLikeClick(movie: Movie) {
        val currentState = state.value
        if (currentState is DetailListState.Loaded) {
            val isLiked = currentState.movie.isLiked
            val newMovie = currentState.movie.copy(isLiked = isLiked.not())
            state.value = DetailListState.Loaded(newMovie)
        }
    }
}