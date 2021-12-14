package com.example.inostudiocase.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.annotation.ExperimentalCoilApi
import com.example.inostudiocase.DetailMovieScreen
import com.example.inostudiocase.MainScreen
import com.example.inostudiocase.TopAppBar
import com.example.inostudiocase.ui.moviedetails.DetailMovieViewModel
import com.example.inostudiocase.ui.movielist.MovieListViewModel
import kotlinx.coroutines.flow.collect
import com.example.inostudiocase.R
import com.example.inostudiocase.common.Screen
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.launch

@ExperimentalCoilApi
@ExperimentalPagerApi
@Preview
@Composable
fun InostudioCaseApp() {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    Scaffold(
        scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState),
        topBar = { TopAppBar(navController = navController) },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.MainScreen.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = Screen.MainScreen.route) {
                MainScreen(navController = navController)
                {
                    scope.launch{
                        snackbarHostState.showSnackbar(it)
                    }
                }
            }

            composable(
                route = Screen.DetailMovieScreen.route,
                arguments = Screen.DetailMovieScreen.arguments)
            {
                DetailMovieScreen()
            }
        }
    }
}