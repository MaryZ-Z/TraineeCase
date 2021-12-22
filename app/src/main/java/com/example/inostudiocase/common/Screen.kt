package com.example.inostudiocase.common

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.inostudiocase.R

sealed class Screen(val route: String, val icon: ImageVector, @StringRes val text: Int){
    object MainScreen: Screen("main_screen", Icons.Default.Home, R.string.home)

    class DetailMovieScreen: Screen(route, Icons.Default.KeyboardArrowRight, R.string.details){
        companion object {
            private const val ROUTE = "movie"
            const val MOVIE_ID = "movieId"
            val route = "$ROUTE/{$MOVIE_ID}"
            val arguments = listOf(navArgument(MOVIE_ID) {type = NavType.IntType})

            fun navigate(movieId: Int) = "$ROUTE/$movieId"
        }
    }

    object Favourite: Screen("favourite", Icons.Default.Favorite, R.string.fav)

    object ActorsList: Screen("actors_list", Icons.Default.Face, R.string.actors)
}