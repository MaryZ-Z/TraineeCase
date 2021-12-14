package com.example.inostudiocase.data

import com.squareup.moshi.Json

data class Image(
    @Json(name = "file_path")
    val filePath: String?,
)
{
    fun posterUrl(): String = "${Movie.IMAGE_BASE_URL}$filePath"
}