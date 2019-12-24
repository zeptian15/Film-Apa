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
        tv_title_show.text = tvShow.title
        tv_release_year_show.text = tvShow.release
        tv_description_show.text = tvShow.description
        Glide.with(this).load(tvShow.background).into(img_background)
        Glide.with(this).load(tvShow.poster).into(img_poster)

        // Set Title Action Bar
        supportActionBar?.title = tvShow.title
    }
}
