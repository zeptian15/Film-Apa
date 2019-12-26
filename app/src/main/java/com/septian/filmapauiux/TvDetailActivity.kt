package com.septian.filmapauiux

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.septian.filmapauiux.model.TvShow
import kotlinx.android.synthetic.main.activity_tv_detail.*

class TvDetailActivity : AppCompatActivity() {
    // Inisialisasi Object
    companion object {
        const val EXTRA_TV_SHOW = "extra_tv_show"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tv_detail)

        // Ambil data
        val tvShow = intent.getParcelableExtra(EXTRA_TV_SHOW) as TvShow

        // Set Value
        tv_title_tv.text = tvShow.title
        tv_description.text = tvShow.description
        tv_caption.text = tvShow.language
        tv_vote.text = tvShow.vote.toString()

        Glide.with(this).load("https://image.tmdb.org/t/p/w500" + tvShow.background)
            .into(img_background)
        Glide.with(this).load("https://image.tmdb.org/t/p/w500" + tvShow.poster).into(img_poster)
    }
}
