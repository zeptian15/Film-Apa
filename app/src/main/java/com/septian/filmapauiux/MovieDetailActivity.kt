package com.septian.filmapauiux

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.septian.filmapauiux.model.Movie
import kotlinx.android.synthetic.main.activity_movie_detail.*

class MovieDetailActivity : AppCompatActivity() {
    // Inisialisasi Object
    companion object {
        const val EXTRA_MOVIE = "extra_movie"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        // Ambil data
        val movie = intent.getParcelableExtra(EXTRA_MOVIE) as Movie

        // Set Value
        tv_title_movie.text = movie.title
        tv_description.text = movie.description
        tv_caption.text = movie.language
        tv_vote.text = movie.vote.toString()
        Glide.with(this).load("https://image.tmdb.org/t/p/w500" + movie.background)
            .into(img_background)
        Glide.with(this).load("https://image.tmdb.org/t/p/w500" + movie.poster).into(img_poster)
    }
}
