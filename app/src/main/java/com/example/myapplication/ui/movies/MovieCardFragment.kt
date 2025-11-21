package com.example.myapplication.ui.movies

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.myapplication.src.movies.domain.Movie

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MovieCard(movie: Movie, editMovie: (Movie) -> Unit, deleteMovie: () -> Unit, onRetrySync: (Movie) -> Unit) {
    var openDialog by remember { mutableStateOf(false) }

    val backgroundColor = if (movie.pendingEvents().isNotEmpty()) {
        MaterialTheme.colors.secondary.copy(alpha = 0.2f)
    } else {
        MaterialTheme.colors.surface
    }

    Card(
        onClick = { editMovie(movie) },
        elevation = 4.dp,
        backgroundColor = backgroundColor,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ID: ${movie.id}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Text(
                    text = movie.title,
                    fontSize = 18.sp,
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = { openDialog = true },
                    modifier = Modifier.zIndex(1f),
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Eliminar película"
                    )
                }
            }
            if (movie.pendingEvents().isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Card(
                    onClick = { onRetrySync(movie) }){
                Text(

                    text = "⚠️ Pendiente de sincronizar (${movie.pendingEvents().size})",
                    color = MaterialTheme.colors.secondaryVariant,
                    fontSize = 14.sp
                )
            }
        }
    }
    if (openDialog) {
            AlertDialog(
                onDismissRequest = { openDialog = false },
                title = {
                    Text(text = "Confirmar eliminación")
                },
                text = {
                    Text("¿Estás seguro de que quieres eliminar esta película?")
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            deleteMovie()
                            openDialog = false
                        }
                    ) {
                        Text("Eliminar")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            openDialog = false
                        }
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}