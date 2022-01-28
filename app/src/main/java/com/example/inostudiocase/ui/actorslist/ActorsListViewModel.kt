package com.example.inostudiocase.ui.actorslist

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
class ActorsListViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {
    val state = mutableStateOf<ListState<Actors>>(ListState.Loading)

    init {
        actors()
        viewModelScope.launch {
            repository.flow.collect {
                val curState = state.value
                if (curState is ListState.Loaded) {
                    state.value = checkLikes(curState.data)
                }
            }
        }
    }

    fun actors() {
        state.value = ListState.Loading
        viewModelScope.launch {
            try {
                val actors = repository.actors()
                state.value = checkLikes(actors)
            } catch (e: Exception) {
                state.value = ListState.Error(e.message.toString())
            }
        }
    }

    fun onLikeClick(actors: Actors) {
        viewModelScope.launch {
            repository.onLikeActorClick(actors)
        }
    }

    private fun checkLikes(actors: List<Actors>): ListState.Loaded<Actors> {
        val newActors = actors.toMutableList().apply {
            replaceAll { actors ->
                actors.copy(isLikedActors = repository.isLikedActors(actors.id))
            }
        }
        return ListState.Loaded(newActors)
    }
}