package com.example.myapplication.src.movies.infrastructure.local.room

import com.example.myapplication.src.movies.domain.Movie
import com.example.myapplication.src.movies.domain.MovieRepository

class MovieLocalRepository(private val movieDao: MovieDao) : MovieRepository {

    override suspend fun addMovie(movie: Movie) {
        val entity = MovieEntity(
            id = movie.id,
            title = movie.title,
        )
        movieDao.insertMovie(entity)
    }

    override suspend fun getAllMovies(): List<Movie> = emptyList()
    override suspend fun updateMovie(movie: Movie) {}
    override suspend fun deleteMovieById(movieId: Int) {}
}