package com.septian.filmapauiux.ui.search


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.SkeletonScreen
import com.google.android.material.snackbar.Snackbar
import com.septian.filmapauiux.DataViewModel
import com.septian.filmapauiux.R
import com.septian.filmapauiux.adapter.TvShowRecyclerAdapter
import com.septian.filmapauiux.model.TvShow
import com.septian.filmapauiux.ui.detail.TvShowDetailActivity
import kotlinx.android.synthetic.main.fragment_search_tv_shows.*

/**
 * A simple [Fragment] subclass.
 */
class SearchTvShows : Fragment() {
    private lateinit var rvTvShow: RecyclerView
    private lateinit var recyclerAdapter: TvShowRecyclerAdapter
    private lateinit var dataViewModel: DataViewModel
    private lateinit var skeletonSearchShow: SkeletonScreen

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_tv_shows, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvTvShow = view.findViewById(R.id.rv_tv)
        rvTvShow.setHasFixedSize(true)

        recyclerAdapter = TvShowRecyclerAdapter()
        recyclerAdapter.notifyDataSetChanged()
        rvTvShow.layoutManager = LinearLayoutManager(context)
        rvTvShow.adapter = recyclerAdapter

        // Inisialisasi DataViewModel
        dataViewModel = ViewModelProviders.of(activity!!).get(DataViewModel::class.java)

        dataViewModel.titleTvShow.observe(viewLifecycleOwner, Observer { tvShowName ->
            if (tvShowName != null) {
                Snackbar.make(rvTvShow, "Hasil : $tvShowName", Snackbar.LENGTH_SHORT).show()
                dataViewModel.setSearchTvShows(tvShowName)
                skeletonSearchShow =
                    Skeleton.bind(rvTvShow).adapter(recyclerAdapter).load(R.layout.item_tv).show()
            }
        })

        dataViewModel.getSearchTvShows().observe(viewLifecycleOwner, Observer { tvItems ->
            if (tvItems != null) {
                recyclerAdapter.setData(tvItems)
                skeletonSearchShow.hide()
            }
        })
        recyclerAdapter.setOnItemClickCallback(object : TvShowRecyclerAdapter.OnItemClickCallback {
            override fun onItemClicked(data: TvShow) {
                moveToDetailActivity(data)
            }
        })

    }

    // Pindah ke Activity Detail Movie
    private fun moveToDetailActivity(tvShow: TvShow) {
        val detailMovieActivity = Intent(context, TvShowDetailActivity::class.java)
        detailMovieActivity.putExtra(TvShowDetailActivity.EXTRA_TV_SHOW, tvShow)
        startActivity(detailMovieActivity)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }
}
