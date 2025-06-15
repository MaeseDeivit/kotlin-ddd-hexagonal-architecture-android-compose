package com.example.myapplication.src.movies.application

import com.example.myapplication.src.movies.domain.Movie
import com.example.myapplication.src.movies.domain.MovieRepository

class MovieServices(private val movieRepository: MovieRepository) {

    suspend fun getAllMovies(): List<Movie> {
        return movieRepository.getAllMovies()
    }

    suspend fun addMovie(movie: Movie): Unit {
        return movieRepository.addMovie(movie)
    }

    suspend fun updateMovie(movie: Movie): Unit {
        return movieRepository.updateMovie(movie)
    }

    suspend fun deleteMovie(movieId: Int): Unit {
        return movieRepository.deleteMovieById(movieId)
    }
}