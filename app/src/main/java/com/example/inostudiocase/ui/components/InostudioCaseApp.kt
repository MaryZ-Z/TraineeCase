package com.example.inostudiocase.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import com.example.inostudiocase.DetailMovieScreen
import com.example.inostudiocase.MainScreen
import com.example.inostudiocase.TopAppBar
import com.example.inostudiocase.common.Screen
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

@InternalCoroutinesApi
@ExperimentalCoilApi
@ExperimentalPagerApi
@Preview
@Composable
fun InostudioCaseApp() {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val items = listOf(Screen.DetailMovieScreen, Screen.MainScreen)
    val bottomItems = listOf(Screen.MainScreen, Screen.ActorsList, Screen.Favourite)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    Scaffold(
        scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState),
        topBar = { TopAppBar(navController = navController) },
        bottomBar = {
            BottomNavigation {
                bottomItems.forEach { item ->
                    BottomNavigationItem(
                        icon = { Icon(imageVector = item.icon, contentDescription = null) },
                        label = {
                            Text(
                                stringResource(item.text),
                                style = MaterialTheme.typography.h5
                            )
                        },
                        selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.MainScreen.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = Screen.MainScreen.route) {
                MainScreen(
                    navController = navController
                ) {
                    scope.launch {
                        snackbarHostState.showSnackbar(it)
                    }
                }
            }

            composable(
                route = Screen.DetailMovieScreen.route,
                arguments = Screen.DetailMovieScreen.arguments
            ) {
                DetailMovieScreen()
            }

            composable(route = Screen.Favourite.route) {
                FavouriteScreen(navController)
            }

            composable(route = Screen.ActorsList.route) {
                ActorsListScreen()
            }
        }
    }
}