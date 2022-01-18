package com.example.inostudiocase.restapi

import com.example.inostudiocase.data.Movie
import com.example.inostudiocase.data.MovieResponse
import com.example.inostudiocase.data.ReviewResponse
import com.example.inostudiocase.data.Reviews
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {
    @GET("discover/movie")
    suspend fun discover(
        @Query("api_key") apiKey: String,
        @Query("language") lang: String,
    ): MovieResponse

    @GET("search/movie")
    suspend fun search(
        @Query("api_key") apiKey: String,
        @Query("language") lang: String,
        @Query("query") query: String,
    ): MovieResponse

    @GET("movie/{movie_id}")
    suspend fun movie(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") lang: String,
        @Query("include_image_language") includeImageLanguage: String,
        @Query("append_to_response") appendToResponse: String,
    ): Movie

    @GET("movie/{movie_id}/reviews")
    suspend fun reviews(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") lang: String,
    ): ReviewResponse
}