package com.septian.filmapauiux

import android.content.ContentValues
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.septian.filmapauiux.db.DatabaseContract
import com.septian.filmapauiux.db.FavouriteHelper
import com.septian.filmapauiux.model.TvShow
import kotlinx.android.synthetic.main.activity_tv_detail.*

class TvDetailActivity : AppCompatActivity(), View.OnClickListener {
    // Inisialisasi Object
    companion object {
        const val EXTRA_TV_SHOW = "extra_tv_show"
    }

    private lateinit var favHelper: FavouriteHelper
    private lateinit var title: String
    private lateinit var description: String
    private lateinit var language: String
    private lateinit var vote: String
    private lateinit var poster: String
    private lateinit var background: String
    private lateinit var type: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tv_detail)

        // Inisialisasi Note Helper
        favHelper = FavouriteHelper.getInstance(applicationContext)
        favHelper.open()

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

        btn_back.setOnClickListener(this)
        btn_fav_tv.setOnClickListener(this)

        // Set Values untuk dimasukan ke DB
        title = tvShow.title.toString()
        description = tvShow.description.toString()
        language = tvShow.language.toString()
        vote = tvShow.vote.toString()
        poster = tvShow.poster.toString()
        background = tvShow.background.toString()
        type = "tv"
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_back -> {
                finish()
            }
            R.id.btn_fav_tv -> {
                val values = ContentValues()
                values.put(DatabaseContract.NoteColumns.TITLE, title)
                values.put(DatabaseContract.NoteColumns.DESCRIPTION, description)
                values.put(DatabaseContract.NoteColumns.LANGUAGE, language)
                values.put(DatabaseContract.NoteColumns.VOTE, vote)
                values.put(DatabaseContract.NoteColumns.POSTER, poster)
                values.put(DatabaseContract.NoteColumns.BACKGROUND, background)
                values.put(DatabaseContract.NoteColumns.TYPE, type)

                // Masukan ke database
                val result = favHelper.insert(values)
                if(result > 0){
                    Snackbar.make(container_detail_tv, R.string.success, Snackbar.LENGTH_SHORT).show()
                } else {
                    Snackbar.make(container_detail_tv, R.string.fail, Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }
}
