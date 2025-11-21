package com.example.myapplication.src.movies.application

import android.util.Log
import com.example.myapplication.src.movies.domain.Movie
import com.example.myapplication.src.movies.domain.MovieLocalRepositoryInterface
import com.example.myapplication.src.movies.domain.MovieRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieSyncService @Inject constructor(
    private val localRepository: MovieLocalRepositoryInterface,
    private val remoteRepository: MovieRepository
) {
    suspend fun getAllMovies(): List<Movie> {
        return try {
            // 1️⃣ Obtenemos las películas del backend remoto
            val remoteMovies = remoteRepository.getAllMovies()

            // 2️⃣ Guardamos las remotas en local (sin sobreescribir las locales sin sincronizar)
            localRepository.replaceAllWith(remoteMovies)

            // 3️⃣ Añadimos las locales sin sincronizar
            val unsynced = localRepository.getUnsyncedMovies()

            // 4️⃣ Devolvemos la mezcla
            (remoteMovies + unsynced).distinctBy { it.id }
        } catch (e: Exception) {
            Log.e("MovieServices", "Error fetching remote movies: ${e.message}")
            // Si falla el remoto, devolvemos todo lo local
            localRepository.getAllMovies()
        }
    }

    suspend fun syncPendingMovies() {
        val moviesPending = localRepository.getPendingMovies()
        for (movie in moviesPending) {
            try {
                remoteRepository.addMovie(movie)
                localRepository.markAsSynced(movie.id)
                Log.d("MovieSyncService", "Synced movie: ${movie.title}")
            } catch (e: Exception) {
                Log.e("MovieSyncService", "Failed to sync ${movie.title}: ${e.message}")
            }
        }
    }
}