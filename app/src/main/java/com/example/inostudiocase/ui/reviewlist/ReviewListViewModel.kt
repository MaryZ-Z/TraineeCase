package com.example.inostudiocase.ui.reviewlist

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inostudiocase.common.ListState
import com.example.inostudiocase.common.Screen
import com.example.inostudiocase.data.Reviews
import com.example.inostudiocase.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewListViewModel @Inject constructor(
    private val repository: MovieRepository,
    savedState: SavedStateHandle
) : ViewModel() {
    val state = mutableStateOf<ListState<Reviews>>(ListState.Loading)
    private val movieId = savedState.get<Int>(Screen.ReviewsList.MOVIE_ID) ?: 0

    init {
        reviews()
    }

    fun reviews() {
        state.value = ListState.Loading
        viewModelScope.launch {
            try {
                val reviews = repository.reviews(movieId)
                state.value = ListState.Loaded(reviews)
            } catch (e: Exception) {
                state.value = ListState.Error(e.message.toString())
            }
        }
    }
}