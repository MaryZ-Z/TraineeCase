package com.example.inostudiocase.common

import androidx.navigation.NavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(val route: String){
    object MainScreen: Screen("main_screen")

    class DetailMovieScreen: Screen(route){
        companion object {
            private const val ROUTE = "movie"
            const val MOVIE_ID = "movieId"
            val route = "$ROUTE/{$MOVIE_ID}"
            val arguments = listOf(navArgument(MOVIE_ID) {type = NavType.IntType})

            fun navigate(movieId: Int) = "$ROUTE/$movieId"
        }
    }
}