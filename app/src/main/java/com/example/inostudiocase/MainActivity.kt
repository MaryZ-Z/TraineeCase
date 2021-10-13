package com.example.inostudiocase

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.rememberImagePainter
import com.example.inostudiocase.ui.theme.*
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MovieViewModel by viewModels()
    private val detailViewModel: DetailViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent() {
            InostudioCaseTheme {
                val navController = rememberNavController()
                val snackbarHostState = remember { SnackbarHostState() }
                val state = viewModel.state.value
                Scaffold(
                    scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState),
                    floatingActionButtonPosition = FabPosition.Center,
                    topBar = { TopAppBar(navController = navController) },
                    floatingActionButton = {
                        if (state is ListState.Error) {
                            FloatingActionButton(
                                onClick = { viewModel.refresh() }
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_baseline_refresh_24),
                                    ""
                                )
                            }
                        }
                    }

                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "MainScreen"
                    ) {
                        composable("MainScreen") {
                            MainScreen(viewModel = viewModel, navController = navController)
                        }
                        composable(
                            "DetailMovie/{movieId}",
                            arguments = listOf(navArgument("movieId")
                            { type = NavType.IntType })
                        ) { backStackEntry ->
                            DetailMovie(
                                viewModel = detailViewModel, navController = navController,
                                backStackEntry.arguments?.getInt("movieId"),
                                onLikeClick = { viewModel.onLikeClick(it) }
                            )
                        }
                    }
                    LaunchedEffect("error") {
                        viewModel.showError.collect {
                            it?.let {
                                snackbarHostState.showSnackbar(
                                    it
                                )
                            }
                        }
                    }
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
            placeholder = { Text(text = "Поиск по названию фильма") },
        )
    }

    //Карточка фильма
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
                Spacer(modifier = Modifier.width(15.dp))
                Text(
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    text = movie.title,
                    fontSize = 22.sp,
                    color = GreenText
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    text = movie.overview,
                    color = Color.DarkGray
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
                        color = Color.Gray
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

    @Composable
    fun MainScreen(viewModel: MovieViewModel, navController: NavController) {
        val state = viewModel.state.value
        Column {
            val query = viewModel.query.value

            Search(query) { viewModel.search(it) }
            if (state == ListState.Loading) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = MyGreen
                )
            }
            when (state) {
                is ListState.Loaded -> ScreenLoaded(state.movies,
                    onRefresh = { viewModel.refresh() },
                    onMovieClick = {
                        navController.navigate("DetailMovie/${it.id}")
                    },
                    onLikeClick = { viewModel.onLikeClick(it) })
                is ListState.Empty -> ScreenNoResults(text = query)
                is ListState.Error -> ScreenError(state.message)
            }
        }
    }


    @Preview(showBackground = true)
    @Composable
    fun MovieItemPreview() {
        //MovieItem()
    }

    @Composable
    fun DetailMovie(
        viewModel: DetailViewModel, navController: NavController, movieId: Int?,
        onLikeClick: (Movie) -> Unit
    ) {
        LaunchedEffect(key1 = movieId, block = { viewModel.movie(movieId!!) })
        val state = detailViewModel.state.value
        Column {
            when (state) {
                is DetailListState.Loading -> ScreenLoading()
                is DetailListState.Loaded -> Movie(state.movie) {
                    onLikeClick.invoke(it)
                    viewModel.onLikeClick(it)
                }
                is DetailListState.Error -> ScreenError(state.message)
            }
        }
    }

    @Composable
    fun Credits(items: List<Credit>) {
        Row(
            modifier =
            Modifier
                .padding(14.dp)
        ) {
            Text(
                text = "В ролях:",
                color = GreenText,
                fontSize = 22.sp
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
                        color = Color.Gray
                    )
                    Text(
                        text = credits.character,
                        color = Color.LightGray
                    )
                }
            }
        }
    }

    @Composable
    fun Posters(items: List<Posters>) {
        Row(
            modifier =
            Modifier
                .padding(14.dp)
        ) {
            Text(
                text = "Скриншоты:",
                color = GreenText,
                fontSize = 22.sp
            )
        }
        LazyRow {
            items(items) { posters ->
                Column(
                    Modifier.padding(14.dp)
                ) {
                    Image(
                        modifier =
                        Modifier
                            .width(200.dp)
                            .height(140.dp),
                        painter = rememberImagePainter(posters.posterUrl()),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }

    @Composable
    fun Reviews(reviews: Reviews) {
        Row(
            modifier =
            Modifier
                .padding(14.dp)
        ) {
            Text(
                text = "Рецензии:",
                color = GreenText,
                fontSize = 22.sp
            )
        }
        Column(
            Modifier.padding(14.dp)
        ) {
            Text(
                overflow = TextOverflow.Ellipsis,
                text = reviews.author,
                color = GreenText
            )
            Text(
                overflow = TextOverflow.Ellipsis,
                text = reviews.rating.toString(),
                color = Color.Yellow,
                textAlign = TextAlign.Left
            )
            Text(
                overflow = TextOverflow.Ellipsis,
                text = reviews.content,
                color = Color.DarkGray
            )
        }
    }

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
            //.padding(14.dp)
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
                        contentDescription = null,
                        //contentScale = ContentScale.FillWidth
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = movie.formattedDate(),
                        color = Color.Gray
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
                        fontSize = 22.sp,
                        color = GreenText
                    )
                    Row() {
                        IconButton(onClick = {
                            onLikeClick.invoke(movie)
                            //viewModel.onLikeClick(movie)
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
                        color = Color.DarkGray
                    )
                }
            }
            Credits(items = movie.credits!!.cast)
            movie.images?.posters?.let { if (it.isNotEmpty()) Posters(items = it) }
            movie.reviews?.results?.firstOrNull()
                ?.let { Reviews(reviews = it) } // Берем только 1 элемент из списка
        }
    }

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
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    painter = painterResource(R.drawable.ic_no_found_dissatisfied_24),
                    contentDescription = null,
                    tint = Color.Red
                )
                Text(
                    text = "По запросу \"$text\" ничего не найдено",
                    color = Color.DarkGray,
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    @Composable
    fun ScreenError(message: String) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    painter = painterResource(R.drawable.ic_baseline_error_outline_24),
                    contentDescription = null,
                    tint = Color.Red
                )
                Text(text = message, color = Color.DarkGray, textAlign = TextAlign.Center)
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
        if (currentScreen != "MainScreen") {
            TopAppBar(
                title = {
                    Text(text = "Фильм")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    }
}