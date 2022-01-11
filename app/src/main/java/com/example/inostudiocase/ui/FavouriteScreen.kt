package com.example.inostudiocase.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import com.example.inostudiocase.MovieItem
import com.example.inostudiocase.R
import com.example.inostudiocase.ScreenLoading
import com.example.inostudiocase.common.ListState
import com.example.inostudiocase.common.Screen
import com.example.inostudiocase.data.Movie
import com.example.inostudiocase.ui.favouritelist.FavouriteMovieViewModel

@ExperimentalCoilApi
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
            is ListState.Empty -> ScreenEmpty()
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

@Composable
fun ScreenEmpty() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                painter = painterResource(R.drawable.ic_no_found_dissatisfied_24),
                contentDescription = null,
                tint = Color.Red
            )
            Text(
                text = stringResource(R.string.no_favor),
                style = MaterialTheme.typography.body2
            )
        }
    }
}