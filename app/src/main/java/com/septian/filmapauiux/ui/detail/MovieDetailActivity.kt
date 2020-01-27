package com.septian.filmapauiux.ui.detail

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteException
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.septian.filmapauiux.R
import com.septian.filmapauiux.db.DataHelper
import com.septian.filmapauiux.db.DatabaseContract
import com.septian.filmapauiux.db.DatabaseContract.NoteColumns.Companion.CONTENT_URI
import com.septian.filmapauiux.model.Movie
import kotlinx.android.synthetic.main.activity_movie_detail.*

class MovieDetailActivity : AppCompatActivity(), View.OnClickListener {
    // Inisialisasi Object
    companion object {
        const val EXTRA_MOVIE = "extra_movie"
    }

    private lateinit var favHelper: DataHelper
    private lateinit var id: String
    private lateinit var title: String
    private lateinit var description: String
    private lateinit var language: String
    private lateinit var vote: String
    private lateinit var poster: String
    private lateinit var background: String
    private lateinit var type: String
    private lateinit var uriWithId: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        // Inisialisasi Note Helper
        favHelper = DataHelper.getInstance(applicationContext)
        favHelper.open()

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

        btn_back.setOnClickListener(this)
        btn_fav_movie.setOnClickListener(this)

        // Set data
        setData(movie)

        // Gunakan Content Provider
        uriWithId = Uri.parse("$CONTENT_URI/${movie.id}")

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_back -> {
                finish()
            }
            R.id.btn_fav_movie -> {
                // Masukan ke database
                if (checkItem()) {
                    addToFavourite()
                } else {
                    Snackbar.make(
                        container_detail_movie,
                        R.string.data_exist, Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        favHelper.close()
    }

    private fun checkItem(): Boolean {
        val cursor = contentResolver?.query(uriWithId, null, null, null, null) as Cursor
        if (cursor.moveToFirst()) return false
        cursor.close()
        return true
    }

    private fun addToFavourite() {
        val values = ContentValues()
        values.put(DatabaseContract.NoteColumns.ID, id)
        values.put(DatabaseContract.NoteColumns.TITLE, title)
        values.put(DatabaseContract.NoteColumns.DESCRIPTION, description)
        values.put(DatabaseContract.NoteColumns.LANGUAGE, language)
        values.put(DatabaseContract.NoteColumns.VOTE, vote)
        values.put(DatabaseContract.NoteColumns.POSTER, poster)
        values.put(DatabaseContract.NoteColumns.BACKGROUND, background)
        values.put(DatabaseContract.NoteColumns.TYPE, type)

        try {
            contentResolver?.insert(CONTENT_URI, values)
            Snackbar.make(
                container_detail_movie,
                R.string.success,
                Snackbar.LENGTH_SHORT
            ).show()
        } catch (e: SQLiteException) {
            Log.d("masuk", e.message.toString())
        }
    }

    private fun setData(movie: Movie) {
        // Set Values untuk dimasukan ke DB
        id = movie.id.toString()
        title = movie.title.toString()
        description = movie.description.toString()
        language = movie.language.toString()
        vote = movie.vote.toString()
        poster = movie.poster.toString()
        background = movie.background.toString()
        type = "movie"
    }
}
