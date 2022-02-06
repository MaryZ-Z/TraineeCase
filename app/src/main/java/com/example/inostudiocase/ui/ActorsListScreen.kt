package com.example.inostudiocase.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.inostudiocase.R
import com.example.inostudiocase.ScreenLoading
import com.example.inostudiocase.common.ListState
import com.example.inostudiocase.common.Screen
import com.example.inostudiocase.data.Actors
import com.example.inostudiocase.data.Movie
import com.example.inostudiocase.ui.actorslist.ActorsListViewModel
import com.example.inostudiocase.ui.components.ScreenError
import com.example.inostudiocase.ui.theme.GreenCard

@ExperimentalCoilApi
@Composable
fun ActorsListScreen(navController: NavController) {
    val listViewModel: ActorsListViewModel = hiltViewModel()
    val state = listViewModel.state.value

    Column {
        when (state) {
            is ListState.Loading -> ScreenLoading()
            is ListState.Loaded -> ScreenLoaded (
                state.data,
                onLikeActorClick = { listViewModel.onLikeClick(it) },
                onActorClick = {navController.navigate(Screen.DetailActorScreen.navigate(it.id)) },
            )
            is ListState.Error -> ScreenError(state.message, onRefresh = { listViewModel.actors() })
        }
    }
}

@ExperimentalCoilApi
@Composable
fun ScreenLoaded(
    items: List<Actors>,
    onLikeActorClick: (Actors) -> Unit,
    onActorClick: (Actors) -> Unit
) {
    LazyColumn {
        items(items) {
            ActorItem(
                actors = it,
                onLikeActorClick = onLikeActorClick,
                onActorClick = onActorClick,
            )
        }
    }
}

//Карточка актера
@ExperimentalCoilApi
@Composable
fun ActorItem(actors: Actors, onLikeActorClick: (Actors) -> Unit, onActorClick: (Actors) -> Unit,) {
    Row(
        Modifier
            .height(IntrinsicSize.Min)
            .clickable { onActorClick.invoke(actors) }
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Image(
            modifier = Modifier
                .width(120.dp)
                .fillMaxHeight(),
            painter = rememberImagePainter(actors.photoUrl()),
            contentDescription = null,
            contentScale = ContentScale.FillWidth
        )
        Column(
            modifier = Modifier
                .background(color = GreenCard)
                .padding(14.dp)
        ) {
            Text(
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                text = actors.name,
                style = MaterialTheme.typography.h1
            )

            Spacer(modifier = Modifier.height(15.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(R.drawable.ic_baseline_star_rate_24),
                    contentDescription = null,
                    tint = Color.Gray
                )
                Spacer(modifier = Modifier.width(7.dp))
                Text(
                    text = actors.popularity.toString(),
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.overline
                )
                IconButton(onClick = {
                    onLikeActorClick.invoke(actors)
                }) {
                    val icon =
                        if (actors.isLikedActors) Icons.Default.Favorite else Icons.Default.FavoriteBorder
                    Icon(imageVector = icon, contentDescription = null, tint = Color.Red)
                }
            }
        }
    }
}