package com.example.inostudiocase.data

import com.example.inostudiocase.data.room.MovieEntity
import com.squareup.moshi.Json
import java.text.SimpleDateFormat
import java.util.*

data class Movie(
    @Json(name = "poster_path")
    val posterPath: String?,
    val adult: Boolean,
    val overview: String,
    val id: Int,
    val title: String,
    @Json(name = "backdrop_path")
    val backdropPath: String?,
    val video: Boolean,
    @Json(name = "release_date")
    val releaseDate: String,
    val isLiked: Boolean = false,
    val credits: CreditsResponse?,
    val images: ImageResponse?,
    val videos: VideosResponse?,
    val reviews: ReviewResponse?
) {
    fun formattedDate(): String {
        val inputFormat = SimpleDateFormat(INPUT_PATTERN, Locale.getDefault())
        val outputFormat = SimpleDateFormat(OUTPUT_PATTERN, Locale.ENGLISH)

        return try {
            val date = inputFormat.parse(releaseDate)
            date?.let { outputFormat.format(it) } ?: kotlin.run { "" }
        } catch (e: Exception) {
            ""
        }
    }

    fun posterUrl(): String = posterPath?.let { "$IMAGE_BASE_URL$it" } ?: kotlin.run{ NO_POSTERS }

    fun toMovieEntity(): MovieEntity = MovieEntity(
        id = id,
        posterPath = posterPath,
        title = title,
        overview = overview,
        isLiked = isLiked
    )

    companion object {
        private const val INPUT_PATTERN = "yyyy-MM-dd"
        private const val OUTPUT_PATTERN = "dd MMMM yyyy"
        private const val NO_POSTERS = "https://www.myvilla.me/tumber.php?src=&w=1120&h=630"
        const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"
    }
}