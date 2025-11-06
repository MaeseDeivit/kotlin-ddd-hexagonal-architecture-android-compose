package com.example.myapplication.src.movies.application

import com.example.myapplication.src.movies.domain.Movie
import com.example.myapplication.src.movies.domain.MovieRepository

class MovieServices(
    private val localRepository: MovieRepository,
    private val movieRepository: MovieRepository,

    ) {

    suspend fun getAllMovies(): List<Movie> {
        return movieRepository.getAllMovies()
    }

    suspend fun addMovie(movie: Movie): Unit {
        localRepository.addMovie(movie)
        try {
            movieRepository.addMovie(movie)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun updateMovie(movie: Movie): Unit {
        return movieRepository.updateMovie(movie)
    }

    suspend fun deleteMovie(movieId: Int): Unit {
        return movieRepository.deleteMovieById(movieId)
    }
}