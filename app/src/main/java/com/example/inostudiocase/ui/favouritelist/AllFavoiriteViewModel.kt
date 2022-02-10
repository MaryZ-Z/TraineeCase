package com.example.inostudiocase.ui.favouritelist

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.inostudiocase.common.ListState
import com.example.inostudiocase.data.Actors
import com.example.inostudiocase.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AllFavouriteViewModel @Inject constructor(
    private val repository: MovieRepository,
) : ViewModel() {
    val state = mutableStateOf<ListState<Actors>>(ListState.Loading)
}