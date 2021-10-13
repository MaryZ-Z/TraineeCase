package com.example.inostudiocase

import com.squareup.moshi.Json
import java.text.SimpleDateFormat
import java.util.*

data class Movie(
    @Json(name = "poster_path")
    val posterPath: String,
    val adult: Boolean,
    val overview: String,
    val id: Int,
    val title: String,
    @Json( name = "backdrop_path")
    val backdropPath: String,
    val video: Boolean,
    @Json(name = "release_date")
    val releaseDate: String,
    val isLiked: Boolean = false,
    val credits: CreditsResponse?,
    val images: PosterResponse?,
    val videos: VideosResponse?,
    val reviews: ReviewResponse?
)
{
    fun formattedDate(): String {
        val inputFormat = SimpleDateFormat(INPUT_PATTERN, Locale.getDefault())
        val outputFormat = SimpleDateFormat(OUTPUT_PATTERN, Locale.getDefault())

        return try {
            val date = inputFormat.parse(releaseDate)
            date?.let { outputFormat.format(it) } ?: kotlin.run { "" }
        } catch (e: Exception) {
            ""
        }
    }

    fun posterUrl(): String = "${Movie.BASE_URL}$posterPath"

    companion object {
        private const val INPUT_PATTERN = "yyyy-MM-dd"
        private const val OUTPUT_PATTERN = "dd MMMM yyyy"
        const val BASE_URL = "https://image.tmdb.org/t/p/w500"
    }
}

data class Credit (
    val name: String,
    val character: String,
    @Json(name = "profile_path")
    val profilePath: String?,
)
{
    fun posterUrl(): String = "${Movie.BASE_URL}$profilePath"
}

data class Posters(
    @Json(name = "file_path")
    val filePath: String?,
)
{
    fun posterUrl(): String = "${Movie.BASE_URL}$filePath"
}

data class Reviews(
    val author: String,
    val rating: Int?,
    val content: String
)

data class Videos(
    val site: String,
    val key: String
)

data class VideosResponse(
    val results: List<Videos>
)

data class ReviewResponse(
    val results: List<Reviews>
)

data class PosterResponse(
    val posters: List<Posters>
)

data class CreditsResponse(
    val cast: List<Credit>
)

data class MovieResponse(
    val results: List<Movie>
)