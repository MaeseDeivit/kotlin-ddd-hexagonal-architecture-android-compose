package com.example.myapplication.src.movies.infrastructure.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.src.events.domain.Event
import com.example.myapplication.src.events.infrastructure.local.room.EventEntity
import com.example.myapplication.src.movies.domain.Movie
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Query("SELECT * FROM movies WHERE id = :id")
    suspend fun getMovieById(id: String): MovieEntity?
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieEntity)
    @Query("DELETE FROM movies WHERE id = :movieId")
    suspend fun deleteMovieById(movieId: String)

    @Query("SELECT * FROM movies ORDER BY id DESC")
    fun getAllMovies(): Flow<List<MovieEntity>>
    @Query("SELECT * FROM movies")
    suspend fun getPendingMovies(): List<MovieEntity>

    @Update
    suspend fun update(movie: MovieEntity)

    fun MovieEntity.toDomain() = Movie(id, title)

    fun Movie.toEntity(isSynced: Boolean = true) = MovieEntity(
        id = id,
        title = title,
    )

    @Query("SELECT * FROM movies")
    suspend fun getUnsyncedMovies(): List<MovieEntity>
    @Query("DELETE FROM movies")
    suspend fun clearAllMovies()
    suspend fun replaceAllWith(movies: List<MovieEntity>) {
        movies.forEach { insertMovie(it) }
    }
}