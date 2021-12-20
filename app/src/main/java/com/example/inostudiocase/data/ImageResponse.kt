package com.example.inostudiocase.data

data class ImageResponse(
    val posters: List<Image>,
    val backdrops: List<Image>
) {
    fun allImages(): List<Image> = posters.plus(backdrops)
}
