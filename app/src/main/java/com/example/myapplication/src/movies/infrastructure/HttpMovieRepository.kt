package com.example.myapplication.src.movies.infrastructure

import com.example.myapplication.src.movies.domain.Movie
import com.example.myapplication.src.movies.domain.MovieRepository
import com.example.myapplication.src.shared.infrastructure.GlobalHttpRepository
import io.ktor.client.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.*

class HttpMovieRepository(client: HttpClient) : GlobalHttpRepository(client),
    MovieRepository {

    companion object {
        private const val MOVIES_ENDPOINT = "/movies"
    }

    override suspend fun getAllMovies(): List<Movie> {
        val response: HttpResponse = request(HttpMethod.Get, MOVIES_ENDPOINT)
        val json = Json.parseToJsonElement(response.bodyAsText()).jsonArray

        return json.map {
            Movie(
                id = it.jsonObject["id"]?.jsonPrimitive?.int ?: 0,
                title = it.jsonObject["title"]?.jsonPrimitive?.content ?: "",
            )
        }
    }

    override suspend fun addMovie(movie: Movie): Unit {
        val response: HttpResponse =
            request(
                HttpMethod.Post, MOVIES_ENDPOINT, mapOf(
                    "title" to movie.title
                )
            )
        if (response.status != HttpStatusCode.Created) {
            throw Exception("Error al guardar la película")
        }
    }

    override suspend fun updateMovie(movie: Movie): Unit {
        val response: HttpResponse =
            request(
                HttpMethod.Put, "$MOVIES_ENDPOINT/${movie.id}", mapOf(
                    "title" to movie.title
                )
            )
        if (response.status != HttpStatusCode.OK) {
            throw Exception("Error al actualizar la película")
        }
    }

    override suspend fun deleteMovieById(movieId: Int): Unit {
        val response: HttpResponse = request(HttpMethod.Delete, "$MOVIES_ENDPOINT/$movieId")
        if (response.status != HttpStatusCode.OK) {
            throw Exception("Error al eliminar la película")
        }
    }
}