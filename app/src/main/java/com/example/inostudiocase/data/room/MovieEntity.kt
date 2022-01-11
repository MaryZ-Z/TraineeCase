package com.example.inostudiocase.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.inostudiocase.data.Movie
import java.util.*

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey(autoGenerate = true) val key: Int = 0,
    val id: Int,
    val posterPath: String?,
    val title: String,
    val overview: String,
    val isLiked: Boolean = true,
    val releaseDate: String
){
    fun toMovie(): Movie = Movie(
        id = id,
        posterPath = posterPath,
        title = title,
        overview = overview,
        isLiked = isLiked,
        adult = false,
        backdropPath = null,
        credits = null,
        video = false,
        images = null,
        releaseDate = releaseDate,
        reviews = null,
        videos = null
    )
}
