package com.example.inostudiocase

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.inostudiocase.common.DetailState
import com.example.inostudiocase.common.Screen
import com.example.inostudiocase.data.ActorImage
import com.example.inostudiocase.data.Actors
import com.example.inostudiocase.data.MovieCredits
import com.example.inostudiocase.ui.actordetails.ActorDetailsViewModel
import com.example.inostudiocase.ui.components.ScreenError
import com.example.inostudiocase.ui.theme.GreenCard
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager

@ExperimentalCoilApi
@ExperimentalPagerApi
@Composable
fun DetailActorScreen(navController: NavController) {
    val viewModel: ActorDetailsViewModel = hiltViewModel()
    val state = viewModel.state.value
    Column {
        when (state) {
            is DetailState.Loading -> ScreenLoading()
            is DetailState.Loaded -> Actor(
                actors = state.data,
                onLikeActorClick = {
                    viewModel.onLikeActorClick(it)
                },
                onMovieClick = {
                    navController.navigate(Screen.DetailMovieScreen.navigate(it.id))
                }
            )
            is DetailState.Error -> ScreenError(state.message, onRefresh = { viewModel.actors() })
        }
    }
}

//детали актера
@ExperimentalCoilApi
@ExperimentalPagerApi
@Composable
fun Actor(
    actors: Actors,
    onLikeActorClick: (Actors) -> Unit,
    onMovieClick: (MovieCredits) -> Unit
) {
    val scrollState = rememberScrollState()
    Column(
        modifier =
        Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(color = GreenCard)
    ) {
        Row(
            modifier =
            Modifier
                .padding(14.dp)
        ) {
            Column {
                Spacer(modifier = Modifier.height(16.dp))
                Image(
                    modifier =
                    Modifier
                        .width(130.dp)
                        .height(200.dp),
                    painter = rememberImagePainter(actors.photoUrl()),
                    contentDescription = null
                )
            }
            Column(
                modifier =
                Modifier
                    .background(color = GreenCard)
                    .padding(12.dp)
            ) {
                Text(
                    overflow = TextOverflow.Ellipsis,
                    text = actors.name,
                    style = MaterialTheme.typography.h1
                )
                Row {
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
        actors.images?.profiles?.let { if (it.isNotEmpty()) ActorImage(items = it) }
        actors.movieCredits?.cast?.let { MovieList(items = it, onMovieClick = onMovieClick) }
    }
}

//Все фото актера
@ExperimentalCoilApi
@ExperimentalPagerApi
@Composable
fun ActorImage(items: List<ActorImage>) {
    Column(
        modifier =
        Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier =
            Modifier
                .padding(14.dp)
        ) {
            Text(
                text = stringResource(R.string.posters),
                style = MaterialTheme.typography.h1
            )
        }
        HorizontalPager(
            count = items.size,
            Modifier
                .height(300.dp)
                .fillMaxWidth()
        ) { page ->
            Image(
                modifier =
                Modifier
                    .fillMaxSize(),
                painter = rememberImagePainter(items[page].posterUrl()),
                contentDescription = null,
                contentScale = ContentScale.FillHeight
            )
        }
    }
}

//Список фильмов, где снимался актер
@ExperimentalCoilApi
@Composable
fun MovieList(items: List<MovieCredits>, onMovieClick: (MovieCredits) -> Unit) {
    Row(
        modifier =
        Modifier
            .padding(14.dp)
    ) {
        Text(
            text = stringResource(R.string.part),
            style = MaterialTheme.typography.h1
        )
    }
    LazyRow {
        items(items) {
            MovieItem(
                movieCredits = it,
                onMovieClick = onMovieClick
            )
        }
    }
}

//Карточка фильма
@ExperimentalCoilApi
@Composable
fun MovieItem(movieCredits: MovieCredits, onMovieClick: (MovieCredits) -> Unit) {
    Row(
        Modifier
            .height(200.dp)
            .clickable { onMovieClick.invoke(movieCredits) }
            .padding(horizontal = 8.dp, vertical = 4.dp)) {

        Image(
            modifier =
            Modifier
                .width(120.dp)
                .fillMaxHeight(),
            painter = rememberImagePainter(movieCredits.posterUrl()),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Column(
            modifier =
            Modifier
                .background(color = GreenCard)
                .padding(14.dp)
                .width(200.dp)
        ) {
            Text(
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                text = movieCredits.title,
                style = MaterialTheme.typography.h1
            )
        }
    }
}