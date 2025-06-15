package com.example.myapplication.ui.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.di.ServiceLocator
import com.example.myapplication.src.movies.domain.Movie
import com.example.myapplication.src.movies.infrastructure.MoviesViewModel
import com.example.myapplication.ui.shared.SharedViewModel
import kotlinx.coroutines.launch

class EditMovieFragment : Fragment() {

    private val moviesViewModel: MoviesViewModel by activityViewModels()
    private var movie by mutableStateOf(Movie.emptyMovie())
    private val movieService = ServiceLocator.movieServices
    private var errorMessages by mutableStateOf(mapOf<String, String?>())
    private var toastMessage by mutableStateOf<String?>(null)
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val movieId = arguments?.getInt("movieId") ?: 0
        movie = moviesViewModel.getMovieById(movieId)!!
        val originalTitleMovie = movie.title

        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    EditMovieCard(
                        originalTitleMovie = originalTitleMovie,
                        movie = movie,
                        errorMessages = errorMessages,
                        onMovieChange = { movie = it },
                        onSave = { updateMovie() },
                        toastMessage = toastMessage
                    )
                }
            }
        }
    }

    private fun updateMovie() {
        val isValid = validateFields()
        if (isValid) {
            lifecycleScope.launch {
                movieService.updateMovie(movie)
                moviesViewModel.updateMovie(movie)
                sharedViewModel.showToast("Película guardada correctamente!")
                findNavController().navigate(R.id.action_editMovieFragment_to_movieListFragment)
            }
        } else {
            sharedViewModel.showToast("Error al guardar la película")
        }
    }

    private fun validateFields(): Boolean {
        val errors = mutableMapOf<String, String?>()
        if (movie.title.isBlank()) {
            errors["title"] = "El título no puede estar vacío"
        }
        errorMessages = errors
        return errors.isEmpty()
    }
}


@Composable
fun EditMovieCard(
    originalTitleMovie: String,
    movie: Movie,
    errorMessages: Map<String, String?>,
    toastMessage: String?,
    onMovieChange: (Movie) -> Unit,
    onSave: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(toastMessage) {
        toastMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

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
                text = "Edit Movie: $originalTitleMovie",
                style = MaterialTheme.typography.h5,
                modifier = Modifier.weight(1f)
            )

            Button(
                content = { Text("Actualizar") },
                onClick = { onSave() }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    value = movie.title,
                    onValueChange = { newTitle ->
                        onMovieChange(movie.copy(title = newTitle))
                    },
                    label = { Text("Title") },
                    isError = errorMessages["title"] != null,
                    modifier = Modifier.fillMaxWidth()
                )
                errorMessages["title"]?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colors.error,
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }
            }
        }
    }
}