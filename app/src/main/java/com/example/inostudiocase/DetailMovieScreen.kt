package com.example.inostudiocase

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.animateContentSize
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
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.inostudiocase.common.DetailState
import com.example.inostudiocase.data.Credit
import com.example.inostudiocase.data.Image
import com.example.inostudiocase.data.Movie
import com.example.inostudiocase.data.Reviews
import com.example.inostudiocase.ui.components.ScreenError
import com.example.inostudiocase.ui.moviedetails.DetailMovieViewModel
import com.example.inostudiocase.ui.theme.GreenCard
import com.example.inostudiocase.ui.theme.MyGreen
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager

@ExperimentalCoilApi
@ExperimentalPagerApi
@Composable
fun DetailMovieScreen() {
    val viewModel: DetailMovieViewModel = hiltViewModel()
    val state = viewModel.state.value
    Column {
        when (state) {
            is DetailState.Loading -> ScreenLoading()
            is DetailState.Loaded -> Movie(state.data) {
                viewModel.onLikeClick(it)
            }
            is DetailState.Error -> ScreenError(state.message, onRefresh = { viewModel.movie() })
        }
    }
}

@ExperimentalCoilApi
@ExperimentalPagerApi
@Composable
fun Movie(
    movie: Movie, onLikeClick: (Movie) -> Unit
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
            Column() {
                Spacer(modifier = Modifier.height(16.dp))
                Image(
                    modifier =
                    Modifier
                        .width(130.dp)
                        .height(200.dp),
                    painter = rememberImagePainter(movie.posterUrl()),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = movie.formattedDate(),
                    style = MaterialTheme.typography.overline
                )

                val context = LocalContext.current
                val video = movie.videos?.results?.firstOrNull()
                if (video?.site == "YouTube") {
                    IconButton(onClick = {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://www.youtube.com/watch?v=${video.key}")
                        )
                        context.startActivity(intent)
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_baseline_ondemand_video_24),
                            contentDescription = null,
                            tint = MyGreen
                        )
                    }
                }
            }
            Column(
                modifier =
                Modifier
                    .background(color = GreenCard)
                    .padding(12.dp)
            ) {
                Text(
                    overflow = TextOverflow.Ellipsis,
                    text = movie.title,
                    style = MaterialTheme.typography.h1
                )
                Row() {
                    IconButton(onClick = {
                        onLikeClick.invoke(movie)
                    }) {
                        val icon =
                            if (movie.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder
                        Icon(imageVector = icon, contentDescription = null, tint = Color.Red)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    overflow = TextOverflow.Ellipsis,
                    text = movie.overview,
                    style = MaterialTheme.typography.subtitle1
                )
            }
        }
        movie.credits?.cast?.let { Credits(items = it) }
        movie.images?.allImages()?.let { if (it.isNotEmpty()) Posters(items = it) }
        Modifier.padding(14.dp)
        // Берем только 1 элемент из списка
        movie.reviews?.results?.firstOrNull()?.let { Reviews(reviews = it) }
    }
}

@ExperimentalCoilApi
@Composable
fun Credits(items: List<Credit>) {
    Row(
        modifier =
        Modifier
            .padding(14.dp)
    ) {
        Text(
            text = stringResource(R.string.roles),
            style = MaterialTheme.typography.h1
        )
    }
    LazyRow {
        items(items) { credits ->
            Column(
                Modifier.padding(14.dp)
            ) {
                Image(
                    modifier =
                    Modifier
                        .width(120.dp)
                        .height(180.dp),
                    painter = rememberImagePainter(credits.posterUrl()),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = credits.name,
                    style = MaterialTheme.typography.overline
                )
                Text(
                    text = credits.character,
                    style = MaterialTheme.typography.h6
                )
            }
        }
    }
}

@ExperimentalCoilApi
@ExperimentalPagerApi
@Composable
fun Posters(items: List<Image>) {
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
        Modifier.height(300.dp)
    ) { page ->
        Image(
            modifier =
            Modifier
                .fillMaxHeight(),
            painter = rememberImagePainter(items[page].posterUrl()),
            contentDescription = null,
            contentScale = ContentScale.FillHeight
        )
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun Reviews(reviews: Reviews) {
    var isExpanded by remember { mutableStateOf(false) }
    Row(
        modifier =
        Modifier
            .padding(14.dp)
    ) {
        Text(
            text = stringResource(R.string.rew),
            style = MaterialTheme.typography.h1
        )
    }
    Column(
        Modifier
            .padding(14.dp)
            .clickable { isExpanded = !isExpanded }
    ) {
        Text(
            overflow = TextOverflow.Ellipsis,
            text = reviews.author,
            style = MaterialTheme.typography.overline
        )
        Text(
            modifier = Modifier.animateContentSize(),
            maxLines = if (isExpanded) Int.MAX_VALUE else 5,
            text = reviews.content,
            style = MaterialTheme.typography.subtitle1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}