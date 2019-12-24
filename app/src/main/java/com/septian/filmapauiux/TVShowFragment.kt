package com.septian.filmapauiux


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.septian.filmapauiux.model.TvShow
import com.septian.rickymaulana.filmapa.adapter.TvAdapter

/**
 * A simple [Fragment] subclass.
 */
class TVShowFragment : Fragment() {
    // Inisialisasi Variabel
    private val list = ArrayList<TvShow>()
    private lateinit var rvTv: RecyclerView

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
        rvTv.setHasFixedSize(true)
        list.addAll(getListMovies())
        showRecyclerList()
    }

    // Method GetListMovies
    private fun getListMovies(): ArrayList<TvShow> {
        val dataTitle = resources.getStringArray(R.array.tv_title)
        val dataRelease = resources.getStringArray(R.array.tv_release_year)
        val dataDescription = resources.getStringArray(R.array.tv_description)
        val dataPoster = resources.obtainTypedArray(R.array.tv_poster)
        val dataBackground = resources.obtainTypedArray(R.array.tv_background)

        val listTv = ArrayList<TvShow>()
        for (position in dataTitle.indices) {
            val tvShow = TvShow(
                dataTitle[position],
                dataRelease[position],
                dataDescription[position],
                dataPoster.getResourceId(position, -1),
                dataBackground.getResourceId(position, -1)
            )
            listTv.add(tvShow)
        }
        return listTv
    }

    // Menampilkan RecyclerView
    private fun showRecyclerList() {
        rvTv.layoutManager = LinearLayoutManager(activity)
        val listTvAdapter = TvAdapter(list)
        rvTv.adapter = listTvAdapter

        listTvAdapter.setOnItemClickCallback(object : TvAdapter.OnItemClickCallback {
            override fun onItemClicked(data: TvShow) {
                moveToDetailActivity(data)
            }
        })
    }

    // Pindah ke Activity Detail Tv Show
    private fun moveToDetailActivity(tvShow: TvShow) {
        val detailTvActivity = Intent(context, TvDetailActivity::class.java)
        detailTvActivity.putExtra(TvDetailActivity.EXTRA_TV_SHOW, tvShow)
        startActivity(detailTvActivity)
    }

}
