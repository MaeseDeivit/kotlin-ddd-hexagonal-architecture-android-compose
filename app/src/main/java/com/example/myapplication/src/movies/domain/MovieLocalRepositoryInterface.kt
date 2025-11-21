package com.example.myapplication.src.movies.domain

import kotlinx.coroutines.flow.Flow

interface MovieLocalRepositoryInterface : MovieRepository {
    fun observeAllMovies(): Flow<List<Movie>>
    suspend fun findMovieById(id: String): Movie?
    override suspend fun addMovie(movie: Movie)
    override suspend fun deleteMovieById(movieId: String)
    suspend fun getPendingMovies(): List<Movie>
    suspend fun markAsSynced(movieId: String): Unit
    suspend fun getUnsyncedMovies(): List<Movie>
    suspend fun replaceAllWith(movies: List<Movie>): Unit
}