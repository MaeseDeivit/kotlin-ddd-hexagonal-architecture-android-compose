package com.example.myapplication.src.movies.infrastructure.local

import com.example.myapplication.src.events.infrastructure.local.room.EventLocalRepository
import com.example.myapplication.src.movies.domain.Movie
import com.example.myapplication.src.movies.domain.MovieLocalRepositoryInterface
import com.example.myapplication.src.movies.infrastructure.local.room.MovieDao
import com.example.myapplication.src.movies.infrastructure.local.room.MovieEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class MovieLocalRepository(
    private val dao: MovieDao,
    private val eventRepository: EventLocalRepository,

) : MovieLocalRepositoryInterface {
    override suspend fun findMovieById(id: String): Movie? {
        val entity = dao.getMovieById(id)
        val pendingEvents = withContext(Dispatchers.IO) {
            eventRepository.getPendingEventsByEntityId(id)
        }
        return entity?.let { Movie(it.id, it.title, pendingEvents) }
    }
    override fun observeAllMovies(): Flow<List<Movie>> =
        dao.getAllMovies().map { entities ->
            entities.map {
                val pendingEvents = withContext(Dispatchers.IO) {
                    eventRepository.getPendingEventsByEntityId(it.id)
                }
                Movie(it.id, it.title, pendingEvents)
            }
        }

    override suspend fun addMovie(movie: Movie) {
        dao.insertMovie(
            MovieEntity(
                id = movie.id,
                title = movie.title,
            )
        )
    }

    override suspend fun deleteMovieById(movieId: String) {
        dao.deleteMovieById(movieId)
    }

    override suspend fun getPendingMovies(): List<Movie> {
        return  dao.getPendingMovies().map { Movie(it.id, it.title) }
    }

    override suspend fun markAsSynced(movieId: String) {
        val movie = dao.getMovieById(movieId) ?: return
        dao.update(movie.copy())
    }
    override suspend fun getAllMovies(): List<Movie> = emptyList()
    override suspend fun updateMovie(movie: Movie) {
        dao.update(
            MovieEntity(
                id = movie.id,
                title = movie.title,
            )
        )
    }
    override suspend fun getUnsyncedMovies(): List<Movie> {
        return dao.getUnsyncedMovies().map { Movie(it.id, it.title) }
    }
    override suspend fun replaceAllWith(movies: List<Movie>) {
        dao.replaceAllWith(
            movies.map {
                MovieEntity(
                    id = it.id,
                    title = it.title,
                )
            }
        )
    }
}