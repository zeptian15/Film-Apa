package com.septian.filmapauiux.main

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.septian.filmapauiux.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.nav_host_fragment)
        nav_bottom.setupWithNavController(navController)

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val isRemind = prefs.getBoolean("daily_reminder", false)
        val isRelease = prefs.getBoolean("release_today_reminder", false)

        if (isRemind) {
            Log.d("Sapa", "Hai")
        } else {
            Log.d("Sapa", " Tidak Hai")
        }

        if (isRelease) {
            Log.d("Sapa", "Hai Release")
        } else {
            Log.d("Sapa", " Tidak Hai Release")
        }
    }


}
