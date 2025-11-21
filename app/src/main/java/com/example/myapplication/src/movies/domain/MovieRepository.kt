package com.example.myapplication.src.movies.domain

interface MovieRepository {
    suspend fun getAllMovies(): List<Movie>
    suspend fun addMovie(movie: Movie): Unit
    suspend fun updateMovie(movie: Movie): Unit
    suspend fun deleteMovieById(movieId: String): Unit
}