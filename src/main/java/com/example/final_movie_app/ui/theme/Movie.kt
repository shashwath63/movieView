package com.example.final_movie_app.ui.theme

import com.google.gson.annotations.SerializedName



data class Movie(
    val id: Int,
    val title: String,
    val releaseDate: String,
    val posterPath: String,
    val overview: String
)
