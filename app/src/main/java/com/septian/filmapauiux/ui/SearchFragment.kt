package com.septian.filmapauiux.ui


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.tabs.TabLayout
import com.septian.filmapauiux.DataViewModel
import com.septian.filmapauiux.R
import com.septian.filmapauiux.adapter.SearchPagerAdapter
import kotlinx.android.synthetic.main.fragment_search.*

/**
 * A simple [Fragment] subclass.
 */
class SearchFragment : Fragment() {

    private var tabPosition = 0
    private lateinit var dataViewModel: DataViewModel
    private lateinit var title: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi View Pager
        val favViewPager = SearchPagerAdapter(view.context, childFragmentManager)

        view_pager.adapter = favViewPager
        tabs.setupWithViewPager(view_pager)

        // Inisialisasi DataViewModel
        dataViewModel = ViewModelProviders.of(activity!!).get(DataViewModel::class.java)
        setTitle()
        setHintSearch()
    }

    private fun setHintSearch() {
        // Get Tab Position Dinamycally
        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {
                tabPosition = tabs.selectedTabPosition
                getTabPosition()
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                tabPosition = tabs.selectedTabPosition
                getTabPosition()
            }

        })
    }

    private fun getTabPosition(): Int {
        if (tabPosition == 0) {
            edt_search.hint = getString(R.string.title_search_movie)
        } else if (tabPosition == 1) {
            edt_search.hint = getString(R.string.title_search_tv)
        } else {
            edt_search.hint = "Oops! Something Wrong!"
        }
        return tabPosition
    }

    private fun setTitle() {
        // Ubah isi editext secara dinamis
        edt_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                // Cari Berdasarkan Title
                title = text.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })

        edt_search.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (getTabPosition() == 0) {
                    dataViewModel.setTitleMovie(title)
                } else if (getTabPosition() == 1) {
                    dataViewModel.setTitleTvShow(title)
                }
                return true
            }
        })
    }

}
