package com.example.inostudiocase.data.repository

import com.example.inostudiocase.MovieDao
import com.example.inostudiocase.data.Actors
import com.example.inostudiocase.data.Movie
import com.example.inostudiocase.restapi.MovieService
import com.example.inostudiocase.room.ActorsDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

//Создаем репозитории, где описываем все методы взаимодействия Api с БД, для удобства и запуска
@Singleton
class MovieRepository @Inject constructor(
    private val service: MovieService,
    private val dao: MovieDao,
    private val actorsDao: ActorsDao
) {
    var likes: List<Movie> = emptyList()
    var likesActors: List<Actors> = emptyList()
    val flow = MutableSharedFlow<Unit>()

    init {
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            dao.getAll().map { it.map { it.toMovie() } }.collect { movies ->
                likes = movies
                flow.emit(Unit)
            }
        }
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            actorsDao.getAll().map { it.map { it.toActors() } }.collect { actors ->
                likesActors = actors
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

    suspend fun reviews(movieId: Int) = service.reviews(movieId, API_KEY, LANGUAGE).results

    suspend fun delete(movie: Movie) = dao.delete(movie.id)

    suspend fun insertActor(actors: Actors) = actorsDao.insertActor(
        actors.toActorsEntity()
    )

    suspend fun deleteActor(actors: Actors) = actorsDao.deleteActor(actors.id)

    fun isLiked(movieId: Int) = likes.any { it.id == movieId }

    fun isLikedActors(actorsId: Int) = likesActors.any { it.id == actorsId }

    suspend fun onLikeClick(movie: Movie) {
        if (isLiked(movie.id)) {
            delete(movie)
        } else {
            insert(movie)
        }
    }

    suspend fun onLikeActorClick(actors: Actors) {
        if (isLikedActors(actors.id)) {
            deleteActor(actors)
        } else {
            insertActor(actors)
        }
    }

    fun favourite() = dao.getAll().map { it.map { it.toMovie() } }

    fun favouriteActors() = actorsDao.getAll().map { it.map { it.toActors() } }

    suspend fun actors() = service.actors(API_KEY, LANGUAGE).results

    suspend fun detailActors(personId: Int) = service.detailActors(personId, API_KEY, LANGUAGE, APPEND_TO_RESPONSE_ACTORS)

    companion object {
        private const val API_KEY =
            "f1c1fa32aa618e6adc168c3cc3cc6c46"
        private const val LANGUAGE = "en-US"
        private const val INCLUDE_IMAGE_LANGUAGE = "en, null"
        private const val APPEND_TO_RESPONSE = "credits,images,reviews,videos"
        private const val APPEND_TO_RESPONSE_ACTORS = "images,movie_credits"
    }
}