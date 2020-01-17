package com.septian.filmapauiux

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.septian.filmapauiux.adapter.FavouriteViewPagerAdapter
import kotlinx.android.synthetic.main.activity_favourite.*

class FavouriteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite)

        // Inisialisasi View Pager
        val favViewPager = FavouriteViewPagerAdapter(this, supportFragmentManager)

        view_pager.adapter = favViewPager
        tabs.setupWithViewPager(view_pager)

        navigation.selectedItemId = R.id.navigation_Favourite

        // Inisialisasi Botton Nav
        navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_Dashboard -> {
                    val main = Intent(this@FavouriteActivity, MainActivity::class.java)
                    startActivity(main)
                    finish()
                    true
                }
                else -> false
            }
        }
    }
}
