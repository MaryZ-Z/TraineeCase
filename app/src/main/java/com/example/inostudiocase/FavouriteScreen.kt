package com.example.inostudiocase

import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import com.example.inostudiocase.common.Tabs
import com.example.inostudiocase.ui.FavouriteMovieScreen
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import java.nio.file.WatchEvent

@ExperimentalPagerApi
@ExperimentalCoilApi
@Composable
fun FavouriteScreen(navController: NavController) {
    val pagerState = rememberPagerState()
    val tabs = Tabs.values()
    val scope = rememberCoroutineScope()

    Column {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(text = stringResource(id = title.titleResId), style = MaterialTheme.typography.button) },
                    selected = pagerState.currentPage == index,
                    onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                )
            }
        }
        HorizontalPager(
            count = tabs.size,
            state = pagerState,
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.Top
        ) { page ->
            when (page) {
                Tabs.Movies.ordinal -> FavouriteMovieScreen(navController = navController)
                Tabs.Actors.ordinal -> FavouriteActorsScreen(navController = navController)
            }
        }
    }
}