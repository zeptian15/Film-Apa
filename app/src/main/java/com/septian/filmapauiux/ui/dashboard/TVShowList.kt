package com.septian.filmapauiux.ui.dashboard


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.septian.filmapauiux.DataViewModel
import com.septian.filmapauiux.R
import com.septian.filmapauiux.adapter.TvShowRecyclerAdapter
import com.septian.filmapauiux.model.TvShow
import com.septian.filmapauiux.ui.detail.TvShowDetailActivity
import kotlinx.android.synthetic.main.fragment_tvshow.*

/**
 * A simple [Fragment] subclass.
 */
class TVShowList : Fragment() {
    // Inisialisasi Variabel
    private val list = ArrayList<TvShow>()
    private lateinit var rvTv: RecyclerView
    private lateinit var showRecyclerAdapter: TvShowRecyclerAdapter

    private lateinit var dataViewModel: DataViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tvshow, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvTv = view.findViewById(R.id.rv_tv)

        showRecyclerAdapter = TvShowRecyclerAdapter()
        showRecyclerAdapter.notifyDataSetChanged()
        rvTv.layoutManager = LinearLayoutManager(context)
        rvTv.adapter = showRecyclerAdapter

        // Inisialisasi DataViewModel
        dataViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(DataViewModel::class.java)

        // Masukan data ke dalam RecyclerView
        dataViewModel.setTvShows()
        showLoading(true)

        dataViewModel.getTvShows().observe(viewLifecycleOwner, Observer { tvShowItems ->
            if (tvShowItems != null) {
                showRecyclerAdapter.setData(tvShowItems)

                showLoading(false)
            }
        })

        showRecyclerAdapter.setOnItemClickCallback(object :
            TvShowRecyclerAdapter.OnItemClickCallback {
            override fun onItemClicked(data: TvShow) {
                moveToDetailActivity(data)
            }
        })

    }

    // Pindah ke Activity Detail Tv Show
    private fun moveToDetailActivity(tvShow: TvShow) {
        val detailTvActivity = Intent(context, TvShowDetailActivity::class.java)
        detailTvActivity.putExtra(TvShowDetailActivity.EXTRA_TV_SHOW, tvShow)
        startActivity(detailTvActivity)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

}
