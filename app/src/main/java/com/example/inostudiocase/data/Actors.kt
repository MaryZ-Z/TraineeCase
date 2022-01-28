package com.example.inostudiocase.data

import com.example.inostudiocase.data.room.ActorsEntity
import com.squareup.moshi.Json

data class Actors(
    @Json(name = "profile_path")
    val profilePath: String?,
    val id: Int,
    val adult: Boolean,
    val name: String,
    val popularity: Float,
    val isLikedActors: Boolean = false,
) {
    fun toActorsEntity(): ActorsEntity = ActorsEntity(
        id = id,
        profilePath = profilePath,
        adult = adult,
        name = name,
        popularity = popularity,
        isLikedActors = isLikedActors
    )

    fun photoUrl(): String =
        profilePath?.let { "${Movie.IMAGE_BASE_URL}$it" } ?: kotlin.run { Actors.NO_POSTERS }

    companion object {
        private const val NO_POSTERS = "https://www.myvilla.me/tumber.php?src=&w=1120&h=630"
    }
}
