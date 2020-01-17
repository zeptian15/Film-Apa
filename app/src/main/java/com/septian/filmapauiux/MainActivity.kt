package com.septian.filmapauiux

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.septian.filmapauiux.adapter.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Inisialisasi View Pager
        val viewPagerAdapter = ViewPagerAdapter(this, supportFragmentManager)

        view_pager.adapter = viewPagerAdapter
        tabs.setupWithViewPager(view_pager)

        // Ganti Bahasa
        btn_change_language.setOnClickListener(this)

        // Inisialisasi Botton Nav
        navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_Dashboard -> {
                    true
                }
                R.id.navigation_Favourite -> {
                    val favourite = Intent(this@MainActivity, FavouriteActivity::class.java)
                    startActivity(favourite)
                    finish()
                    true
                }
                else -> false
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_change_language -> {
                // Masuk halaman ganti bahasa
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
            }
        }
    }


}
