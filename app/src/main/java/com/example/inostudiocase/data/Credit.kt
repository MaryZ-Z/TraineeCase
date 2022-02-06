package com.example.inostudiocase.data

import com.squareup.moshi.Json

data class Credit(
    val name: String,
    val character: String,
    @Json(name = "profile_path")
    val profilePath: String?,
    val id: Int,
) {
    fun posterUrl(): String = "${Movie.IMAGE_BASE_URL}$profilePath"
}