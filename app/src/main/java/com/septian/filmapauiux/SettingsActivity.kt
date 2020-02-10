package com.septian.filmapauiux

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import kotlinx.android.synthetic.main.settings_activity.*

class SettingsActivity : AppCompatActivity() {

    companion object {
        const val TYPE_REMINDER = "daily_reminder"
        const val TYPE_RELEASE = "release_reminder"

        const val EXTRA_TYPE = "type"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btn_back.setOnClickListener {
            finish()
        }
    }

    class SettingsFragment : PreferenceFragmentCompat(),
        SharedPreferences.OnSharedPreferenceChangeListener {
        // Inisialisasi Variabel
        private var DAILY_REMINDER: String = "daily_reminder"
        private var RELEASE_TODAY: String = "release_today_reminder"
        private lateinit var switchDaily: SwitchPreference
        private lateinit var switchRelease: SwitchPreference

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            init()
            setValues()
        }

        private fun init() {
            switchDaily = findPreference<SwitchPreference>(DAILY_REMINDER) as SwitchPreference
            switchRelease = findPreference<SwitchPreference>(RELEASE_TODAY) as SwitchPreference
        }

        private fun setValues() {
            val reminder = ReminderReceiver()
            val sh = preferenceManager.sharedPreferences

            switchDaily.isChecked = sh.getBoolean(DAILY_REMINDER, false)

            if (switchDaily.isChecked) {
                // Daily Reminder aktif
                Log.d("StatusAlarm", "FromSettings : Activated")
                reminder.activateDailyReminder(requireContext(), true)
            } else {
                // Daily Reminder Tidak
                Log.d("StatusAlarm", "FromSettings : Not Activated")
                reminder.activateDailyReminder(requireContext(), false)
            }


            switchRelease.isChecked = sh.getBoolean(RELEASE_TODAY, false)

            if (switchRelease.isChecked) {
                // Release today aktif
                Log.d("StatusAlarm", "FromSettings : Activated")
                reminder.activateReleaseToday(requireContext(), true)
            } else {
                // Release today tidak aktif
                Log.d("StatusAlarm", "FromSettings : Not Activated")
                reminder.activateReleaseToday(requireContext(), false)
            }
        }

        override fun onResume() {
            super.onResume()
            preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        }

        override fun onPause() {
            super.onPause()
            preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        }

        override fun onSharedPreferenceChanged(
            sharedPreferences: SharedPreferences,
            key: String?
        ) {
            val reminder = ReminderReceiver()
            when (key) {
                DAILY_REMINDER -> {
                    switchDaily.isChecked =
                        sharedPreferences.getBoolean(DAILY_REMINDER, false)
                    if (switchDaily.isChecked) {
                        // Daily Reminder aktif
                        Log.d("StatusAlarm", "FromSettings : Activated")
                        reminder.activateDailyReminder(requireContext(), true)
                    } else {
                        // Daily Reminder Tidak
                        Log.d("StatusAlarm", "FromSettings : Not Activated")
                        reminder.activateDailyReminder(requireContext(), false)
                    }
                }

                RELEASE_TODAY -> {
                    switchRelease.isChecked =
                        sharedPreferences.getBoolean(RELEASE_TODAY, false)
                    if (switchRelease.isChecked) {
                        // Release today aktif
                        Log.d("StatusAlarm", "FromSettings : Activated")
                        reminder.activateReleaseToday(requireContext(), true)
                    } else {
                        // Release today tidak aktif
                        Log.d("StatusAlarm", "FromSettings : Not Activated")
                        reminder.activateReleaseToday(requireContext(), false)
                    }
                }
            }
        }
    }
}