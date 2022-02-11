package com.example.inostudiocase.ui.favouritelist

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inostudiocase.common.ListState
import com.example.inostudiocase.data.Actors
import com.example.inostudiocase.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteActorsViewModel @Inject constructor(
    private val repository: MovieRepository,
) : ViewModel() {
    val state = mutableStateOf<ListState<Actors>>(ListState.Loading)

    init {
        viewModelScope.launch {
            repository.favouriteActors().collect { actors ->
                state.value = if (actors.isEmpty()) ListState.Empty else ListState.Loaded(actors)
            }
        }
    }

    fun onLikeActorClick(actors: Actors) {
        viewModelScope.launch {
            repository.onLikeActorClick(actors)
        }
    }
}