package com.septian.filmapauiux.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.septian.filmapauiux.BuildConfig
import com.septian.filmapauiux.R
import com.septian.filmapauiux.model.Movie
import kotlinx.android.synthetic.main.item_movie.view.*

class MovieRecyclerAdapter :
    RecyclerView.Adapter<MovieRecyclerAdapter.MovieViewHolder>() {
    // Inisialisasi Item Click
    private var onItemClickCallback: OnItemClickCallback? = null
    private val mData = ArrayList<Movie>()

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(items: ArrayList<Movie>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MovieViewHolder {
        val view =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.item_movie, viewGroup, false)
        return MovieViewHolder(view)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: Movie) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(BuildConfig.URL_IMAGES + movie.poster)
                    .apply(RequestOptions().override(550, 550))
                    .into(img_poster)
                Glide.with(itemView.context)
                    .load(BuildConfig.URL_IMAGES + movie.background)
                    .apply(RequestOptions().override(550, 550))
                    .into(img_background)
                tv_title_movie.text = movie.title

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(movie) }
            }
        }
    }

    // Inisialisasi Interface OnItemClick
    interface OnItemClickCallback {
        fun onItemClicked(data: Movie)
    }
}