package com.septian.filmapauiux

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.septian.filmapauiux.db.DatabaseContract
import com.septian.filmapauiux.db.FavouriteHelper
import com.septian.filmapauiux.model.Favourite
import kotlinx.android.synthetic.main.activity_favourite_detail.*

class FavouriteDetailActivity : AppCompatActivity(), View.OnClickListener {
    // Inisialisasi Object
    companion object {
        const val EXTRA_FAVOURITE = "extra_favourite"
        const val EXTRA_POSITION = "extra_position"
        const val RESULT_DELETE = 301
        const val REQUEST_DELETE = 101
        const val ALERT_DIALOG_CLOSE = 10
        const val ALERT_DIALOG_DELETE = 20
    }
    private lateinit var favHelper: FavouriteHelper
    private var position: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite_detail)

        // Inisialisasi Note Helper
        favHelper = FavouriteHelper.getInstance(applicationContext)
        favHelper.open()

        // Ambil data
        val fav = intent.getParcelableExtra(EXTRA_FAVOURITE) as Favourite

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
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_back -> {
                finish()
            }
            R.id.btn_delete_fav -> {
                val fav = intent.getParcelableExtra(EXTRA_FAVOURITE) as Favourite

                val result = favHelper.deleteById(fav.id.toString()).toLong()
                if(result > 0){
                    val intent = Intent(this@FavouriteDetailActivity, FavouriteActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(applicationContext, R.string.fail_del, Toast.LENGTH_SHORT).show()
                }

            }
        }
    }
}
