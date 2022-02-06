package com.example.inostudiocase.data

import com.squareup.moshi.Json

data class MovieCredits(
    val title: String,
    @Json(name = "poster_path")
    val posterPath: String?,
    val id: Int
) {
    fun posterUrl(): String = "${Movie.IMAGE_BASE_URL}$posterPath"
}