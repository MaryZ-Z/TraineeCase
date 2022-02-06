package com.example.inostudiocase.ui.actordetails

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inostudiocase.common.DetailState
import com.example.inostudiocase.common.Screen
import com.example.inostudiocase.data.Actors
import com.example.inostudiocase.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActorDetailsViewModel @Inject constructor(
    private val repository: MovieRepository,
    private val savedState: SavedStateHandle
) : ViewModel() {
    val state = mutableStateOf<DetailState<Actors>>(DetailState.Loading)
    private val personId = savedState.get<Int>(Screen.DetailActorScreen.PERSON_ID) ?: 0

    init {
        actors()
        viewModelScope.launch {
            repository.flow.collect {
                val curState = state.value
                if (curState is DetailState.Loaded) {
                    state.value = checkLikes(curState.data)
                }
            }
        }
    }

    fun actors() {
        state.value = DetailState.Loading
        viewModelScope.launch {
            try {
                val details = repository.detailActors(personId)
                state.value = checkLikes(details)
            } catch (e: Exception) {
                state.value = DetailState.Error(e.message.toString())
            }
        }
    }

    fun onLikeActorClick(actors: Actors) {
        viewModelScope.launch {
            repository.onLikeActorClick(actors)
        }
    }

    private fun checkLikes(actors: Actors): DetailState.Loaded<Actors> {
        val newActor = actors.copy(isLikedActors = repository.isLikedActors(actors.id))
        return DetailState.Loaded(newActor)
    }
}