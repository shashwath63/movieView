package com.example.final_movie_app.adapter
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.example.final_movie_app.MovieDetailsActivity
import com.example.final_movie_app.R
import com.example.final_movie_app.databinding.ItemMoviesBinding
import com.example.final_movie_app.response.MoviesListResponse
import com.example.final_movie_app.utils.Constants.POSTER_BASE_URL

class MoviesAdapter : RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {

    private lateinit var binding: ItemMoviesBinding
    private lateinit var context: Context
    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemMoviesBinding.inflate(inflater, parent, false)
        context = parent.context
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ViewHolder : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: MoviesListResponse.Result) {
            binding.apply {
                tvMovieName.text = item.title
                tvMovieDateRelease.text = item.releaseDate
                tvRate.text = item.voteAverage.toString()
                val moviePosterURL = POSTER_BASE_URL + item?.posterPath
                ImgMovie.load(moviePosterURL) {
                    crossfade(true)
                    placeholder(R.drawable.poster_placeholder)
                    scale(Scale.FILL)
                }
                tvLang.text = item.originalLanguage

                root.setOnClickListener {
                    onItemClickListener?.onItemClick(item.id)
                }

                btnAddReview.setOnClickListener {
                    onItemClickListener?.onAddReviewClick(item.id)
                }
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<MoviesListResponse.Result>() {
        override fun areItemsTheSame(
            oldItem: MoviesListResponse.Result,
            newItem: MoviesListResponse.Result
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: MoviesListResponse.Result,
            newItem: MoviesListResponse.Result
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    interface OnItemClickListener {
        fun onItemClick(movieId: Int)
        fun onAddReviewClick(movieId: Int)
    }
}
