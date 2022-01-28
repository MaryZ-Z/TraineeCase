package com.example.inostudiocase.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.inostudiocase.data.Actors

@Entity(tableName = "actors")
data class ActorsEntity(
    @PrimaryKey(autoGenerate = true) val key: Int = 0,
    val id: Int,
    val profilePath: String?,
    val adult: Boolean,
    val name: String,
    val popularity: Float,
    val isLikedActors: Boolean
) {
    fun toActors(): Actors = Actors(
        id = id,
        profilePath = profilePath,
        adult = adult,
        name = name,
        popularity = popularity,
        isLikedActors = true
    )
}
