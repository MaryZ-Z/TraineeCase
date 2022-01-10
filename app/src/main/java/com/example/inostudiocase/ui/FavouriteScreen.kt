package com.example.inostudiocase.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import com.example.inostudiocase.MovieItem
import com.example.inostudiocase.ScreenLoading
import com.example.inostudiocase.common.ListState
import com.example.inostudiocase.common.Screen
import com.example.inostudiocase.data.Movie
import com.example.inostudiocase.ui.favouritelist.FavouriteMovieViewModel
import kotlinx.coroutines.InternalCoroutinesApi

@ExperimentalCoilApi
@InternalCoroutinesApi
@Composable
fun FavouriteScreen(navController: NavController) {
    val viewModel: FavouriteMovieViewModel = hiltViewModel()
    val state = viewModel.state.value
    Column {
        when (state) {
            is ListState.Loading -> ScreenLoading()
            is ListState.Loaded -> FavouriteList(state.data,
                onMovieClick = {
                    navController.navigate(Screen.DetailMovieScreen.navigate(it.id))
                },
                onLikeClick = { viewModel.onLikeClick(it) })
            is ListState.Error -> Unit
            is ListState.Empty -> ScreenLoading() //нужен пустой экран
        }
    }
}

@ExperimentalCoilApi
@Composable
fun FavouriteList(
    items: List<Movie>,
    onMovieClick: (Movie) -> Unit,
    onLikeClick: (Movie) -> Unit
) {
    LazyColumn {
        items(items) { item ->
            MovieItem(
                movie = item,
                onMovieClick = onMovieClick,
                onLikeClick = onLikeClick
            )
        }
    }
}