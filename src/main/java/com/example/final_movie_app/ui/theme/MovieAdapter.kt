package com.example.final_movie_app.ui.theme

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.final_movie_app.R

class MovieAdapter(private val movies: List<Movie>) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val moviePoster: ImageView = itemView.findViewById(R.id.movie_poster)
        val movieTitle: TextView = itemView.findViewById(R.id.movie_title)
        val movieReleaseDate: TextView = itemView.findViewById(R.id.movie_release_date)
        val movieOverview: TextView = itemView.findViewById(R.id.movie_overview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.movieTitle.text = movie.title
        holder.movieReleaseDate.text = movie.releaseDate
        holder.movieOverview.text = movie.overview

        Glide.with(holder.itemView.context)
            .load("https://image.tmdb.org/t/p/w500${movie.posterPath}")
            .into(holder.moviePoster)
    }

    override fun getItemCount(): Int {
        return movies.size
    }
}
