package com.example.final_movie_app
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.final_movie_app.R
import com.example.final_movie_app.ui.theme.Movie
import com.example.final_movie_app.ui.theme.MovieAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var movieRecyclerView: RecyclerView
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        firebaseAuth = FirebaseAuth.getInstance()

        if (firebaseAuth.currentUser == null) {
            startActivity(Intent(this, login::class.java))
            finish()
            return
        }

        movieRecyclerView = findViewById(R.id.movie_recycler_view)
        movieRecyclerView.layoutManager = LinearLayoutManager(this)

        fetchMovies()
    }

    private fun fetchMovies() {
        val apiKey = "34025e173cec2072f6773de93a208c74"
        val url = "https://api.themoviedb.org/3/movie/popular?api_key=$apiKey"

        val request = Request.Builder()
            .url(url)
            .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                if (responseBody != null) {
                    val gson = Gson()
                    val movieListType = object : TypeToken<List<Movie>>() {}.type
                    val movies: List<Movie> = gson.fromJson(responseBody, movieListType)

                    runOnUiThread {
                        movieAdapter = MovieAdapter(movies)
                        movieRecyclerView.adapter = movieAdapter
                    }
                }
            }
        })
    }
}
