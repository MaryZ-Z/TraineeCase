package com.example.inostudiocase.common

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.inostudiocase.R

enum class BottomNav(val route: String, val icon: ImageVector, @StringRes val text: Int) {
    MovieList("movie_list", Icons.Default.Home, R.string.home),
    AllActors("all_actors", Icons.Default.Face, R.string.actors),
    AllFavourite("all_favourite", Icons.Default.Favorite, R.string.fav)
}