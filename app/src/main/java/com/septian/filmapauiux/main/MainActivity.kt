package com.septian.filmapauiux.main

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.septian.filmapauiux.R
import com.septian.filmapauiux.ReminderReceiver
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    companion object {
        const val TYPE_REMINDER = "daily_reminder"
        const val TYPE_RELEASE = "release_reminder"
    }

    private lateinit var reminderReceiver: ReminderReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.nav_host_fragment)
        nav_bottom.setupWithNavController(navController)

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val isRemind = prefs.getBoolean("daily_reminder", false)
        val isRelease = prefs.getBoolean("release_today_reminder", false)

        reminderReceiver = ReminderReceiver()

        if (reminderReceiver.isReminderActivated(this, TYPE_REMINDER)) {
            Log.d("StatusAlarm", "Activated")
        } else {
            Log.d("StatusAlarm", "Not Activated")
        }

        if (reminderReceiver.isReminderActivated(this, TYPE_RELEASE)) {
            Log.d("StatusAlarmRe", "Activated")
        } else {
            Log.d("StatusAlarmRe", "Not Activated")
        }

//        if (isRemind){
//            Log.d("StatusAlarmFromShared", "Activated")
//            reminderReceiver.activateDailyReminder(this, true)
//        } else {
//            Log.d("StatusAlarmFromShared", "Not Activated")
//            reminderReceiver.activateDailyReminder(this, false)
//        }
//
//        if (isRelease){
//            Log.d("StatusAlarmFromShared", "Activated")
//            reminderReceiver.activateReleaseToday(this, true)
//        } else {
//            Log.d("StatusAlarmFromShared", "Not Activated")
//            reminderReceiver.activateReleaseToday(this, false)
//        }

    }


}
