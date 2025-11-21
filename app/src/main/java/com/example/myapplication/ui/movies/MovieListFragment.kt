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
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.src.movies.application.MovieServices
import com.example.myapplication.src.movies.application.MovieSyncService
import com.example.myapplication.src.movies.domain.Movie
import com.example.myapplication.src.movies.infrastructure.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MovieListFragment : Fragment() {

    private val moviesViewModel: MoviesViewModel by viewModels()
    @Inject
    lateinit var movieSyncService: MovieSyncService
    @Inject lateinit var movieService: MovieServices

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val navController = findNavController()

        return ComposeView(requireContext()).apply {
            setContent {
                val moviesState by moviesViewModel.movies.collectAsState()

                MaterialTheme {
                    MovieListScreen(
                        movies = moviesState,
                        navController = navController,
                        editMovie = { movie -> openEditMovieFragment(movie) },
                        deleteMovie = { movie -> deleteMovie(movie) },
                        onRetrySync = { movie -> onRetrySync(movie) }
                    )
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()
    }

    private suspend fun getAllMovies(): List<Movie> {
        return movieSyncService.getAllMovies()
    }

    fun openEditMovieFragment(movie: Movie) {
        findNavController().navigate(MovieListFragmentDirections.actionMovieListFragmentToEditMovieFragment(movie))
    }

    fun deleteMovie(movie: Movie) {
        lifecycleScope.launch {
            movieService.deleteMovie(movie)
        }
    }
    fun onRetrySync(movie: Movie) {
        lifecycleScope.launch {
            movieService.syncMoviePendingEvents(movie)
        }
    }
}

@Composable
fun MovieListScreen(
    movies: List<Movie>,
    navController: NavController,
    editMovie: (Movie) -> Unit,
    deleteMovie: (Movie) -> Unit,
    onRetrySync: (Movie) -> Unit
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
                        editMovie = { editMovie(movies[index]) },
                        deleteMovie = { deleteMovie(movies[index]) },
                        onRetrySync = { onRetrySync(movies[index]) }
                    )
                }
            }
        }
    }
}