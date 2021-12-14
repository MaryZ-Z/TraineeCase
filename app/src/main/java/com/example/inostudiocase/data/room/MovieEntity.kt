package com.example.inostudiocase.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey(autoGenerate = true) val key: Int = 0,
    val id: Int,
    val posterPath: String?,
    val title: String,
    val overview: String,
    val isLiked: Boolean = true
)