package com.example.myapplication.src.movies.infrastructure

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.src.movies.application.MovieServices
import com.example.myapplication.src.movies.domain.Movie
import com.example.myapplication.src.movies.domain.MovieLocalRepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val movieServices: MovieServices
) : ViewModel() {

    val movies: StateFlow<List<Movie>> = movieServices.getAllMoviesFlow()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun refreshMovies() {
        viewModelScope.launch {
            try {
                movieServices.getAllMovies() // esto actualizará Room
            } catch (e: Exception) {
                Log.e("MoviesViewModel", "Failed to refresh movies: ${e.message}")
            }
        }
    }

    fun deleteMovie(movie: Movie) {
        viewModelScope.launch {
            try {
                movieServices.deleteMovie(movie)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun getMovieById(id: String): Movie? {
        Log.d("MoviesViewModel", "${id} Películas actuales: ${movies.value}")
        Log.d("MoviesViewModel", "all movies: ${movies.value.map { it.id }}")
        return movies.value.find { it.id == id }
    }
}