package com.example.myapplication.ui.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.*
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
import com.example.myapplication.src.movies.application.MovieServices
import com.example.myapplication.src.movies.domain.Movie
import com.example.myapplication.src.movies.infrastructure.MoviesViewModel
import com.example.myapplication.ui.shared.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.getValue

@AndroidEntryPoint
class AddMovieFragment : Fragment() {
    private val moviesViewModel: MoviesViewModel by activityViewModels()
    private var movie: Movie by mutableStateOf(Movie.emptyMovie())
    private var errorMessages by mutableStateOf(mapOf<String, String?>())
    private var toastMessage by mutableStateOf<String?>(null)
    private val sharedViewModel: SharedViewModel by activityViewModels()
    @Inject lateinit var movieService: MovieServices

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    AddMovieCard(
                        movie = movie,
                        errorMessages = errorMessages,
                        onMovieChange = { movie = it },
                        onSave = { addNewMovie() },
                        toastMessage = toastMessage
                    )
                }
            }
        }
    }

    private fun addNewMovie() {
        val isValid = validateFields()
        if (isValid) {
            lifecycleScope.launch {
                movieService.addMovie(movie)
                sharedViewModel.showToast("Película guardada correctamente!")
                findNavController().navigate(R.id.action_addMovieFragment_to_movieListFragment)
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
fun AddMovieCard(
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
                text = "Add Movie",
                style = MaterialTheme.typography.h5,
                modifier = Modifier.weight(1f)
            )
            Button(
                content = { Text("Save") },
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