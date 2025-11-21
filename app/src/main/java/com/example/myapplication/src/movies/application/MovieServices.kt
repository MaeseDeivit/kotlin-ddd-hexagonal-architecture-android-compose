package com.example.myapplication.src.movies.application

import android.util.Log
import com.example.myapplication.src.events.application.EventServices
import com.example.myapplication.src.events.domain.Event
import com.example.myapplication.src.movies.domain.Movie
import com.example.myapplication.src.movies.domain.MovieLocalRepositoryInterface
import com.example.myapplication.src.movies.domain.MovieRepository
import kotlinx.coroutines.flow.Flow
import java.util.UUID
class MovieServices(
    private val localRepository: MovieLocalRepositoryInterface,
    private val movieRepository: MovieRepository,
    private val eventServices: EventServices,
    ) {
    fun getAllMoviesFlow(): Flow<List<Movie>> = localRepository.observeAllMovies()
    suspend fun getAllMovies(): List<Movie> {
        return try {
            // 1️⃣ Obtenemos las películas del backend remoto
            val remoteMovies = movieRepository.getAllMovies()

            // 3️⃣ Añadimos las locales sin sincronizar
            val unsynced = localRepository.getUnsyncedMovies()

            // 2️⃣ Guardamos las remotas en local (sin sobreescribir las locales sin sincronizar)
            localRepository.replaceAllWith(remoteMovies)

            // 4️⃣ Devolvemos la mezcla
            (remoteMovies + unsynced).distinctBy { it.id }
        } catch (e: Exception) {
            Log.e("MovieServices", "Error fetching remote movies: ${e.message}")
            // Si falla el remoto, devolvemos todo lo local
            localRepository.getAllMovies()
        }
    }

    suspend fun addMovie(movie: Movie): Unit {
        try{
        movieRepository.addMovie(movie)
        localRepository.addMovie(movie)
    } catch (e: Exception) {
            Log.d("MovieServices localRepository save", "Remote failed, saving local only")
            Log.d("MovieServices ERROR", e.message.toString())
            localRepository.addMovie(movie)
            val event = Event(
                id = UUID.randomUUID().toString(),
                entityId = movie.id,
                entityType = "Movie",
                operationType = "CREATE",
                payload = """{"id":"${movie.id}","title":"${movie.title}"}""",
                timestamp = System.currentTimeMillis(),
                synced = false
            )
            movie.addEvent(event)
            localRepository.updateMovie(movie)
            val updatedMovie = movie.copy(title = "99999999")
            localRepository.updateMovie(updatedMovie)
            return eventServices.create(event)
    }
    }

    suspend fun updateMovie(movie: Movie): Unit {
        try{
            movieRepository.updateMovie(movie)
            localRepository.updateMovie(movie)
        } catch (e: Exception) {
            Log.d("MovieServices localRepository update", "Remote failed, update local only")
            Log.d("MovieServices ERROR", e.message.toString())
            val event = Event(
                id = UUID.randomUUID().toString(),
                entityId = movie.id,
                entityType = "Movie",
                operationType = "UPDATE",
                payload = """{"id":"${movie.id}","title":"${movie.title}"}""",
                timestamp = System.currentTimeMillis(),
                synced = false
            )
            movie.addEvent(event)
           // localRepository.updateMovie(movie)
            return eventServices.create(event)
        }
    }

    suspend fun deleteMovie(movie: Movie): Unit {
        try{
            movieRepository.deleteMovieById(movie.id)
            localRepository.deleteMovieById(movie.id)
        } catch (e: Exception) {
            Log.d("MovieServices localRepository delete", "Remote failed, delete local only")
            Log.d("MovieServices ERROR", e.message.toString())
            val event = Event(
                id = UUID.randomUUID().toString(),
                entityId = movie.id,
                entityType = "Movie",
                operationType = "DELETE",
                payload = """{"id":"${movie.id}"}""",
                timestamp = System.currentTimeMillis(),
                synced = false
            )
            movie.addEvent(event)
            return eventServices.create(event)
        }
    }

    suspend fun syncMoviePendingEvents(movie: Movie):Unit {
        for (event in movie.pendingEvents()) {
            try {
                when (event.operationType) {
                    "CREATE" -> {
                        movieRepository.addMovie(movie)
                    }
                    "UPDATE" -> {
                        movieRepository.updateMovie(movie)
                    }
                    "DELETE" -> {
                        movieRepository.deleteMovieById(movie.id)
                        localRepository.deleteMovieById(movie.id)
                    }
                }
                eventServices.markEventAsSynced(event)
                movie.removeEvent(event)
            } catch (e: Exception) {
                Log.e("MovieServices", "Error syncing event ${event.id} for movie ${movie.id}: ${e.message}")
            }
        }
        localRepository.updateMovie(movie)
    }
}