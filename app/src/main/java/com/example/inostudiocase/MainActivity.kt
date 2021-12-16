package com.example.inostudiocase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.inostudiocase.common.ListState
import com.example.inostudiocase.common.Screen
import com.example.inostudiocase.data.Movie
import com.example.inostudiocase.ui.InostudioCaseApp
import com.example.inostudiocase.ui.components.ScreenError
import com.example.inostudiocase.ui.movielist.MovieListViewModel
import com.example.inostudiocase.ui.theme.GreenCard
import com.example.inostudiocase.ui.theme.InostudioCaseTheme
import com.example.inostudiocase.ui.theme.MyGreen
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
@ExperimentalPagerApi
@ExperimentalCoilApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InostudioCaseTheme() {
                InostudioCaseApp()
            }
        }
    }
}

//Строка поиска
@Composable
fun Search(text: String, onTextChange: (String) -> Unit) {
    TextField(
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = MyGreen
            )
        },
        value = text,
        shape = RoundedCornerShape(100.dp),
        onValueChange = { onTextChange.invoke(it) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = GreenCard,
            cursorColor = Color.DarkGray,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        placeholder = { Text(text = stringResource(R.string.search)) },
    )
}

//Карточка фильма
@ExperimentalCoilApi
@Composable
fun MovieItem(movie: Movie, onMovieClick: (Movie) -> Unit, onLikeClick: (Movie) -> Unit) {
    Row(
        Modifier
            .height(IntrinsicSize.Min)
            .clickable { onMovieClick.invoke(movie) }
            .padding(horizontal = 8.dp, vertical = 4.dp)) {

        Image(
            modifier =
            Modifier
                .width(120.dp)
                .fillMaxHeight(),
            painter = rememberImagePainter(movie.posterUrl()),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Column(
            modifier =
            Modifier
                .background(color = GreenCard)
                .padding(14.dp)
        ) {
            Text(
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                text = movie.title,
                style = MaterialTheme.typography.h1
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                text = movie.overview,
                style = MaterialTheme.typography.subtitle1
            )
            Spacer(modifier = Modifier.height(15.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(R.drawable.ic_baseline_calendar_today_24),
                    contentDescription = null,
                    tint = Color.Gray
                )
                Spacer(modifier = Modifier.width(7.dp))
                Text(
                    text = movie.formattedDate(),
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.overline
                )
                IconButton(onClick = {
                    onLikeClick.invoke(movie)
                }) {
                    val icon =
                        if (movie.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder
                    Icon(imageVector = icon, contentDescription = null, tint = Color.Red)
                }
            }
        }
    }
}

@ExperimentalCoilApi
@Composable
fun MainScreen(navController: NavController, showSnackbar: (String) -> Unit) {
    val listViewModel: MovieListViewModel = hiltViewModel()
    val state = listViewModel.state.value
    Column {
        val query = listViewModel.query.value

        Search(query) { listViewModel.search(it) }
        if (state == ListState.Loading) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth(),
                color = MyGreen
            )
        }
        when (state) {
            is ListState.Loaded -> ScreenLoaded(state.data,
                onRefresh = { listViewModel.onRefresh() },
                onMovieClick = {
                    navController.navigate(Screen.DetailMovieScreen.navigate(it.id))
                },
                onLikeClick = { listViewModel.onLikeClick(it) })
            is ListState.Empty -> ScreenNoResults(text = query)
            is ListState.Error -> ScreenError(state.message,
                onRefresh = { listViewModel.onRefresh() })
        }
        LaunchedEffect("error") {
            listViewModel.showError.collect {
                it?.let { showSnackbar(it) }
            }
        }
    }
}

@ExperimentalCoilApi
@Composable
fun ScreenLoaded(
    items: List<Movie>,
    onRefresh: () -> Unit,
    onMovieClick: (Movie) -> Unit,
    onLikeClick: (Movie) -> Unit
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(false),
        onRefresh = { onRefresh.invoke() },
    ) {
        LazyColumn {
            items(items) {
                MovieItem(
                    movie = it,
                    onMovieClick = onMovieClick,
                    onLikeClick = onLikeClick
                )
            }
        }
    }
}

@Composable
fun ScreenNoResults(text: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                painter = painterResource(R.drawable.ic_no_found_dissatisfied_24),
                contentDescription = null,
                tint = Color.Red
            )
            Text(
                text = stringResource(R.string.no_found, text),
                style = MaterialTheme.typography.body2
            )
        }
    }
}

@Composable
fun ScreenLoading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = MyGreen)
    }
}


@Composable
fun TopAppBar(navController: NavHostController) {
    val backstackEntry = navController.currentBackStackEntryAsState()
    val currentScreen = backstackEntry.value?.destination?.route?.substringBefore("/")
    if (currentScreen != Screen.MainScreen.route) {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.movie),
                    style = MaterialTheme.typography.h5
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
            }
        )
    }
}