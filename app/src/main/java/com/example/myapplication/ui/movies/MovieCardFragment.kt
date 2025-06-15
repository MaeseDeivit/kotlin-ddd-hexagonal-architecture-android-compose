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
fun MovieCard(movie: Movie, editMovie: () -> Unit, deleteMovie: () -> Unit) {
    var openDialog by remember { mutableStateOf(false) }

    Card(
        onClick = { editMovie() },
        elevation = 4.dp,
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
                    text = movie.title,
                    fontSize = 18.sp,
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = { openDialog = true }, // Muestra el diálogo al hacer clic
                    modifier = Modifier.zIndex(1f),
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Eliminar película"
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
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