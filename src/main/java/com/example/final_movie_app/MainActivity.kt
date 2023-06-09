package com.example.final_movie_app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.final_movie_app.Api.ApiClient
import com.example.final_movie_app.Api.ApiServices
import com.example.final_movie_app.adapter.MoviesAdapter
import com.example.final_movie_app.databinding.ActivityMainBinding
import com.example.final_movie_app.response.MoviesListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), MoviesAdapter.OnItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private val moviesAdapter by lazy { MoviesAdapter() }
    private val api: ApiServices by lazy {
        ApiClient().getClient().create(ApiServices::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            prgBarMovies.visibility = View.VISIBLE

            val callPopularMoviesApi = api.getPopularMovie(1)
            callPopularMoviesApi.enqueue(object : Callback<MoviesListResponse> {
                override fun onResponse(
                    call: Call<MoviesListResponse>,
                    response: Response<MoviesListResponse>
                ) {
                    prgBarMovies.visibility = View.GONE
                    when (response.code()) {
                        in 200..299 -> {
                            Log.d("Response Code", " success messages : ${response.code()}")
                            response.body()?.let { itBody ->
                                itBody.results.let { itData ->
                                    if (itData.isNotEmpty()) {
                                        moviesAdapter.differ.submitList(itData)
                                        rlMovies.apply {
                                            layoutManager = LinearLayoutManager(this@MainActivity)
                                            adapter = moviesAdapter
                                        }
                                    }
                                }
                            }
                        }
                        // Handle other response codes
                    }
                }

                override fun onFailure(call: Call<MoviesListResponse>, t: Throwable) {
                    prgBarMovies.visibility = View.GONE
                    Log.e("onFailure", "Err : ${t.message}")
                }
            })

            btnPopular.setOnClickListener {
                prgBarMovies.visibility = View.VISIBLE
                val callPopularMoviesApi = api.getPopularMovie(1)
                callPopularMoviesApi.enqueue(object : Callback<MoviesListResponse> {
                    override fun onResponse(
                        call: Call<MoviesListResponse>,
                        response: Response<MoviesListResponse>
                    ) {
                        prgBarMovies.visibility = View.GONE
                        when (response.code()) {
                            in 200..299 -> {
                                Log.d("Response Code", " success messages : ${response.code()}")
                                response.body()?.let { itBody ->
                                    itBody.results.let { itData ->
                                        if (itData.isNotEmpty()) {
                                            moviesAdapter.differ.submitList(itData)
                                            rlMovies.apply {
                                                layoutManager = LinearLayoutManager(this@MainActivity)
                                                adapter = moviesAdapter
                                            }
                                        }
                                    }
                                }
                            }
                            // Handle other response codes
                        }
                    }

                    override fun onFailure(call: Call<MoviesListResponse>, t: Throwable) {
                        prgBarMovies.visibility = View.GONE
                        Log.e("onFailure", "Err : ${t.message}")
                    }
                })
            }

            btnTrending.setOnClickListener {
                prgBarMovies.visibility = View.VISIBLE
                val callTrendingMoviesApi = api.getTrendingMovies(1)
                callTrendingMoviesApi.enqueue(object : Callback<MoviesListResponse> {
                    override fun onResponse(
                        call: Call<MoviesListResponse>,
                        response: Response<MoviesListResponse>
                    ) {
                        prgBarMovies.visibility = View.GONE
                        when (response.code()) {
                            in 200..299 -> {
                                Log.d("Response Code", " success messages : ${response.code()}")
                                response.body()?.let { itBody ->
                                    itBody.results.let { itData ->
                                        if (itData.isNotEmpty()) {
                                            moviesAdapter.differ.submitList(itData)
                                            rlMovies.apply {
                                                layoutManager = LinearLayoutManager(this@MainActivity)
                                                adapter = moviesAdapter
                                            }
                                        }
                                    }
                                }
                            }
                            // Handle other response codes
                        }
                    }

                    override fun onFailure(call: Call<MoviesListResponse>, t: Throwable) {
                        prgBarMovies.visibility = View.GONE
                        Log.e("onFailure", "Err : ${t.message}")
                    }
                })
            }
        }

        moviesAdapter.setOnItemClickListener(this)
    }

    override fun onItemClick(movieId: Int) {
        val intent = Intent(this, MovieDetailsActivity::class.java)
        intent.putExtra("id", movieId)
        startActivity(intent)
    }

    override fun onAddReviewClick(movieId: Int) {
        val intent = Intent(this, ReviewActivity::class.java)
        intent.putExtra("id", movieId)
        startActivity(intent)
    }
}
