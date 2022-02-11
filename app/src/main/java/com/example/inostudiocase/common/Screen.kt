package com.example.inostudiocase.common

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.inostudiocase.R

sealed class Screen(val route: String) {
    object MainScreen : Screen("main_screen")

    class DetailMovieScreen : Screen(route) {
        companion object {
            private const val ROUTE = "movie"
            const val MOVIE_ID = "movieId"
            val route = "$ROUTE/{$MOVIE_ID}"
            val arguments = listOf(navArgument(MOVIE_ID) { type = NavType.IntType })

            fun navigate(movieId: Int) = "$ROUTE/$movieId"
        }
    }

    object Favourite : Screen("favourite")

    object ActorsList : Screen("actors_list")

    class ReviewsList : Screen(route) {
        companion object {
            private const val ROUTE = "reviews"
            const val MOVIE_ID = "movieId"
            val route = "$ROUTE/{$MOVIE_ID}"
            val arguments = listOf(navArgument(MOVIE_ID) { type = NavType.IntType })

            fun navigate(movieId: Int) = "$ROUTE/$movieId"
        }
    }

    class DetailActorScreen : Screen(route) {
        companion object {
            private const val ROUTE = "detailActors"
            const val PERSON_ID = "personId"
            val route = "$ROUTE/{$PERSON_ID}"
            val arguments = listOf(navArgument(PERSON_ID) { type = NavType.IntType })

            fun navigate(personId: Int) = "$ROUTE/$personId"
        }
    }
}