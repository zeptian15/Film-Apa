package com.septian.filmapauiux.ui


import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.septian.filmapauiux.R
import com.septian.filmapauiux.SettingsActivity
import com.septian.filmapauiux.adapter.MainPagerAdapter
import kotlinx.android.synthetic.main.fragment_dashboard.*

/**
 * A simple [Fragment] subclass.
 */
class DashboardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi View Pager
        val viewPagerAdapter = MainPagerAdapter(view.context, childFragmentManager)

        view_pager.adapter = viewPagerAdapter
        tabs.setupWithViewPager(view_pager)

        btn_change_language.setOnClickListener {
            // Masuk halaman ganti bahasa
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }

        // Settings Notification
        btn_settings.setOnClickListener {
            val mSettings = Intent(this.context, SettingsActivity::class.java)
            startActivity(mSettings)
        }
    }


}
