package com.example.final_movie_app.Api

import com.example.final_movie_app.response.MovieDetails
import com.example.final_movie_app.response.MoviesListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServices {

    //    https://api.themoviedb.org/3/movie/550?api_key=***
    //    https://api.themoviedb.org/3/movie/popular?api_key=***
    //    https://api.themoviedb.org/3/

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id: Int): Call<MovieDetails>

    @GET("movie/popular")
    fun getPopularMovie(@Query("page") page: Int): Call<MoviesListResponse>

    @GET("trending/movie/week")
    fun getTrendingMovies(@Query("page") page: Int): Call<MoviesListResponse>

}