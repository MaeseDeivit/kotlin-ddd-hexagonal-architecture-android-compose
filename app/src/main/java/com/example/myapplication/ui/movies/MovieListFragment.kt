package com.example.myapplication.ui.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.di.ServiceLocator
import com.example.myapplication.src.movies.domain.Movie
import com.example.myapplication.src.movies.infrastructure.MoviesViewModel
import kotlinx.coroutines.launch

class MovieListFragment : Fragment() {

    private val moviesViewModel: MoviesViewModel by activityViewModels()
    private val movieService = ServiceLocator.movieServices
    private var movies: List<Movie> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val navController = findNavController()

        return ComposeView(requireContext()).apply {
            setContent {
                val moviesState by moviesViewModel.movies.collectAsState()
                movies = moviesState

                MaterialTheme {
                    MovieListScreen(
                        movies = movies,
                        navController = navController,
                        editMovie = { movieId -> openEditMovieFragment(movieId) },
                        deleteMovie = { movieId -> deleteMovie(movieId) }
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            if (moviesViewModel.movies.value.isEmpty()) {
                moviesViewModel.loadMovies(getAllMovies())
            }
        }
    }

    private suspend fun getAllMovies(): List<Movie> {
        return movieService.getAllMovies()
    }

    fun openEditMovieFragment(movieId: Int) {
        val action = MovieListFragmentDirections.actionMovieListFragmentToEditMovieFragment(movieId)
        findNavController().navigate(action)
    }

    fun deleteMovie(movieId: Int) {
        lifecycleScope.launch {
            movieService.deleteMovie(movieId)
            moviesViewModel.deleteMovie(movieId)
        }
    }
}

@Composable
fun MovieListScreen(
    movies: List<Movie>,
    navController: NavController,
    editMovie: (Int) -> Unit,
    deleteMovie: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Movies",
                style = MaterialTheme.typography.h5,
                modifier = Modifier.weight(1f)
            )
            Button(
                content = { Text("Add Movie") },
                onClick = {
                    navController.navigate(R.id.action_movieListFragment_to_addMovieFragment)
                },
            )
        }

        if (movies.isEmpty()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(movies.size) { index ->
                    MovieCard(
                        movie = movies[index],
                        editMovie = { editMovie(movies[index].id) },
                        deleteMovie = { deleteMovie(movies[index].id) }
                    )
                }
            }
        }
    }
}