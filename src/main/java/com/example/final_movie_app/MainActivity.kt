package com.example.final_movie_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.final_movie_app.ui.theme.Movie
import com.example.final_movie_app.ui.theme.MovieAdapter
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.IOException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var movieRecyclerView: RecyclerView
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        movieRecyclerView = findViewById(R.id.movie_recycler_view)
        movieRecyclerView.layoutManager = LinearLayoutManager(this)

        fetchMovies()
    }

    private fun fetchMovies() {
        val apiKey = "34025e173cec2072f6773de93a208c74"
        val baseUrl = "https://api.themoviedb.org/3/"

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)

            .client(OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val movieService = retrofit.create(MovieService::class.java)
        val call = movieService.getPopularMovies(apiKey)

        call.enqueue(object : retrofit2.Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.isSuccessful) {
                    val movies = response.body()?.results ?: emptyList()
                    movieAdapter = MovieAdapter(movies)
                    movieRecyclerView.adapter = movieAdapter
                }
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })



    }
}

// Retrofit Service Interface
interface MovieService {
    @GET("movie/popular")
    fun getPopularMovies(@Query("api_key") apiKey: String): Call<MovieResponse>
}

// Response Model for Movie List
data class MovieResponse(
    @SerializedName("results")
    val results: List<Movie>
)

// Movie Model
data class Movie(
    @SerializedName("title")
    val title: String,
    @SerializedName("overview")
    val overview: String,
    @SerializedName("poster_path")
    val posterPath: String
)
