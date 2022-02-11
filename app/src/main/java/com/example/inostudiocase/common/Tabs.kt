package com.example.inostudiocase.common

import androidx.annotation.StringRes
import com.example.inostudiocase.R

enum class Tabs (@StringRes val titleResId: Int) {
    Movies(titleResId = R.string.movies),
    Actors(titleResId = R.string.actors)
}