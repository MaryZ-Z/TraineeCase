package com.example.inostudiocase

import javax.inject.Inject
import javax.inject.Singleton

//Создаем репозитории, где описываем все методы взаимодействия Api с БД, для удобства и запуска
@Singleton
class MovieRepository @Inject constructor(
    private val service: MovieService,
    private val dao: MovieDao
) {
    //список фильмов
    suspend fun discover() = service.discover(API_KEY, LANGUAGE).results

    //Поиск
    suspend fun search(query: String) = service.search(API_KEY, LANGUAGE, query).results

    //movie details
    suspend fun movie(movieId: Int) = service.movie(movieId, API_KEY, LANGUAGE, APPEND_TO_RESPONSE)

    //Избранное
    suspend fun getLikes() = dao.getAll()

    suspend fun insert(movie: Movie) = dao.insert(MovieEntity(id = movie.id))

    suspend fun delete(movie: Movie) = dao.delete(movie.id)

    companion object {
        private const val API_KEY = "f1c1fa32aa618e6adc168c3cc3cc6c46" //API-ключ небезопасно хранить в таком виде
        private const val LANGUAGE = "ru-RU"
        private const val APPEND_TO_RESPONSE = "credits,images,reviews,videos"
    }
}