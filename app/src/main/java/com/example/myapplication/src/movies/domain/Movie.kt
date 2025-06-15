package com.example.myapplication.src.movies.domain

data class Movie(
    val id: Int,
    var title: String,
) {
    override fun toString(): String {
        return "Movie(id=$id, title=${title})"
    }

    companion object {
        fun emptyMovie(): Movie {
            return Movie(
                id = 0,
                title = ""
            )
        }
    }
}