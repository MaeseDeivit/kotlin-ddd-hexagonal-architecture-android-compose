package com.example.myapplication.src.movies.infrastructure

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.src.movies.domain.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor() : ViewModel() {

    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies

    fun loadMovies(movies: List<Movie>) {
        viewModelScope.launch {
            _movies.value = movies
            Log.d("_movies", _movies.value.toString())

        }
    }

    fun addMovieToList(movie: Movie) {
        _movies.value = _movies.value + movie
    }

    fun getMovieById(id: Int): Movie? {
        Log.d("_movies", _movies.value.toString())
        return _movies.value.find { it.id == id }
    }

    fun getMovies(): List<Movie> {
        return _movies.value
    }

    fun updateMovie(movie: Movie) {
        val updatedMovies = _movies.value.toMutableList()
        val index = updatedMovies.indexOfFirst { it.id == movie.id }
        updatedMovies[index] = movie
        _movies.value = updatedMovies
    }

    fun deleteMovie(movieId: Int) {
        val updatedMovies = _movies.value.toMutableList()
        updatedMovies.removeIf { it.id == movieId }
        _movies.value = updatedMovies
    }
}