package com.septian.filmapauiux.ui.favourite

import android.content.Intent
import android.database.sqlite.SQLiteException
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.septian.filmapauiux.R
import com.septian.filmapauiux.db.DatabaseContract.NoteColumns.Companion.CONTENT_URI
import com.septian.filmapauiux.model.Favourite
import kotlinx.android.synthetic.main.activity_favourite_detail.*

class FavouriteDetail : AppCompatActivity(), View.OnClickListener {
    // Inisialisasi Object
    companion object {
        const val EXTRA_FAVOURITE = "extra_favourite"
        const val EXTRA_POSITION = "extra_position"
        const val RESULT_DELETE = 301
        const val ALERT_DIALOG_CLOSE = 10
        const val ALERT_DIALOG_DELETE = 20
    }

    private lateinit var uriWithId: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite_detail)

        // Ambil data
        var fav = intent.getParcelableExtra(EXTRA_FAVOURITE) as Favourite

        // Set Value
        tv_title_favourite.text = fav.title
        tv_description.text = fav.description
        tv_caption.text = fav.language
        tv_vote.text = fav.vote.toString()
        Glide.with(this).load("https://image.tmdb.org/t/p/w500" + fav.background)
            .into(img_background)
        Glide.with(this).load("https://image.tmdb.org/t/p/w500" + fav.poster).into(img_poster)

        btn_back.setOnClickListener(this)
        btn_delete_fav.setOnClickListener(this)

        // Set UriWithId
        uriWithId = Uri.parse("$CONTENT_URI/${fav.id}")
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_back -> {
                finish()
            }
            R.id.btn_delete_fav -> {
                val position = intent.getIntExtra(EXTRA_POSITION, 0)

                try {
                    contentResolver?.delete(uriWithId, null, null)
                } catch (e: SQLiteException) {
                    Log.d("DELETE : ", e.message.toString())
                    Toast.makeText(
                        applicationContext,
                        R.string.fail_del, Toast.LENGTH_SHORT
                    ).show()
                }

                val resultintent = Intent()
                resultintent.putExtra(EXTRA_POSITION, position)
                setResult(RESULT_DELETE, resultintent)
                finish()

            }
        }
    }
}
