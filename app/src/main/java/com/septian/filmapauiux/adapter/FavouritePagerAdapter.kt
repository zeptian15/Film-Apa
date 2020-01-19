package com.septian.filmapauiux.adapter

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.septian.filmapauiux.R
import com.septian.filmapauiux.ui.Favourite.FavouritesMovies
import com.septian.filmapauiux.ui.Favourite.FavouritesTvShow

class FavouritePagerAdapter(private val mContext: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    // Inisialisasi Nama Tab
    @StringRes
    private val TAB_TITLES = intArrayOf(R.string.tab_movie, R.string.tab_tv)

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FavouritesMovies()
            1 -> fragment = FavouritesTvShow()
        }
        return fragment as Fragment
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(TAB_TITLES[position])
    }

}