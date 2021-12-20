package com.example.inostudiocase.data.repository

import com.example.inostudiocase.MovieDao
import com.example.inostudiocase.data.Movie
import com.example.inostudiocase.data.room.MovieEntity
import com.example.inostudiocase.restapi.MovieService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

//Создаем репозитории, где описываем все методы взаимодействия Api с БД, для удобства и запуска
@Singleton
class MovieRepository @Inject constructor(
    private val service: MovieService,
    private val dao: MovieDao
) {
    var likes: List<MovieEntity> = emptyList()
    val flow = MutableSharedFlow<Unit>()

    init {
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            dao.getAll().collect { movies ->
                likes = movies
                flow.emit(Unit)
            }
        }
    }

    //список фильмов
    suspend fun discover() = service.discover(API_KEY, LANGUAGE).results

    //Поиск
    suspend fun search(query: String) = service.search(API_KEY, LANGUAGE, query).results

    //movie details
    suspend fun movie(movieId: Int) =
        service.movie(movieId, API_KEY, LANGUAGE, INCLUDE_IMAGE_LANGUAGE, APPEND_TO_RESPONSE)

    suspend fun insert(movie: Movie) = dao.insert(
        movie.toMovieEntity()
    )

    suspend fun delete(movie: Movie) = dao.delete(movie.id)

    fun isLiked(movieId: Int) = likes.any { it.id == movieId }

    suspend fun onLikeClick(movie: Movie) {
        if (isLiked(movie.id)) {
            delete(movie)
        } else {
            insert(movie)
        }
    }

    companion object {
        private const val API_KEY =
            "f1c1fa32aa618e6adc168c3cc3cc6c46" //API-ключ небезопасно хранить в таком виде
        private const val LANGUAGE = "en-US"
        private const val INCLUDE_IMAGE_LANGUAGE = "en, null"
        private const val APPEND_TO_RESPONSE = "credits,images,reviews,videos"
    }
}