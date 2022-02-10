package com.example.inostudiocase

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
import com.example.inostudiocase.common.ListState
import com.example.inostudiocase.common.Screen
import com.example.inostudiocase.data.Actors
import com.example.inostudiocase.ui.ActorItem
import com.example.inostudiocase.ui.ScreenEmpty
import com.example.inostudiocase.ui.favouritelist.FavouriteActorsViewModel

@Composable
fun FavouriteActorsScreen(navController: NavController) {
    val viewModel: FavouriteActorsViewModel = hiltViewModel()
    val state = viewModel.state.value
    Column {
        when (state) {
            is ListState.Loading -> ScreenLoading()
            is ListState.Loaded -> FavouriteActorsList(state.data,
                onActorClick = {
                    navController.navigate(Screen.DetailActorScreen.navigate(it.id))
                },
                onLikeActorClick = { viewModel.onLikeActorClick(it) })
            is ListState.Error -> Unit
            is ListState.Empty -> ScreenEmpty()
        }
    }
}

@ExperimentalCoilApi
@Composable
fun FavouriteActorsList(
    items: List<Actors>,
    onActorClick: (Actors) -> Unit,
    onLikeActorClick: (Actors) -> Unit
) {
    LazyColumn {
        items(items) { item ->
            ActorItem(
                actors = item,
                onActorClick = onActorClick,
                onLikeActorClick = onLikeActorClick
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