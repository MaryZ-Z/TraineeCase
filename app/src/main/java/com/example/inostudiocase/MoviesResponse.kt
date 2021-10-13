package com.example.inostudiocase

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
        @Query("append_to_response") appendToResponse: String,
    ): Movie
}