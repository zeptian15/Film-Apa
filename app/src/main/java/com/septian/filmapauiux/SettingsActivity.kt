package com.septian.filmapauiux

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import kotlinx.android.synthetic.main.settings_activity.*

class SettingsActivity : AppCompatActivity() {

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
        }

        private fun init() {
            switchDaily = findPreference<SwitchPreference>(DAILY_REMINDER) as SwitchPreference
            switchRelease = findPreference<SwitchPreference>(RELEASE_TODAY) as SwitchPreference
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
            sharedPreferences: SharedPreferences?,
            key: String?
        ) {
            when (key) {
                DAILY_REMINDER -> {
                    switchDaily.isChecked =
                        sharedPreferences?.getBoolean(DAILY_REMINDER, false) ?: false
                    if (switchDaily.isChecked) {
                        Toast.makeText(
                            context,
                            "Anda akan diingatkan untuk membuka aplikasi",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            context,
                            "Anda tidak akan diingatkan untuk membuka aplikasi",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                RELEASE_TODAY -> {
                    switchRelease.isChecked =
                        sharedPreferences?.getBoolean(RELEASE_TODAY, false) ?: false
                    if (switchRelease.isChecked) {
                        Toast.makeText(
                            context,
                            "Anda akan diingatkan untuk release today",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            context,
                            "Anda tidak akan diingatkan untuk release today",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}