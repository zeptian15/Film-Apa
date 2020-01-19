package com.septian.filmapauiux.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.septian.filmapauiux.R
import com.septian.filmapauiux.adapter.FavouritePagerAdapter
import kotlinx.android.synthetic.main.fragment_favourite.*

/**
 * A simple [Fragment] subclass.
 */
class FavouriteFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi View Pager
        val favViewPager = FavouritePagerAdapter(view.context, childFragmentManager)

        view_pager.adapter = favViewPager
        tabs.setupWithViewPager(view_pager)
    }


}
