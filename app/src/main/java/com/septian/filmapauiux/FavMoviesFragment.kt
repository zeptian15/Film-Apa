package com.septian.filmapauiux


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.septian.filmapauiux.adapter.FavouriteAdapter
import com.septian.filmapauiux.db.FavouriteHelper
import com.septian.filmapauiux.helper.MappingHelper
import com.septian.filmapauiux.model.Favourite
import kotlinx.android.synthetic.main.fragment_fav_movies.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class FavMoviesFragment : Fragment() {
    // Inisialisasi FavHelper
    private lateinit var favHelper: FavouriteHelper
    private lateinit var adapter: FavouriteAdapter

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fav_movies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favHelper = FavouriteHelper.getInstance(view.context)
        favHelper.open()

        adapter = FavouriteAdapter()
        adapter.notifyDataSetChanged()
        rv_fav_movie.layoutManager = LinearLayoutManager(context)
        rv_fav_movie.adapter = adapter
        rv_fav_movie.setHasFixedSize(true)

        if (savedInstanceState == null) {
            // proses ambil data
            loadFavMoviesAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<Favourite>(EXTRA_STATE)
            if (list != null) {
                adapter.favList = list
            }
        }

        adapter.setOnItemClickCallback(object : FavouriteAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Favourite) {
                moveToDetailActivity(data)
            }
        })

    }

    // Pindah ke Activity Detail Movie
    private fun moveToDetailActivity(favourite: Favourite) {
        val detailFavActivity = Intent(context, FavouriteDetailActivity::class.java)
        detailFavActivity.putExtra(FavouriteDetailActivity.EXTRA_FAVOURITE, favourite)
        startActivity(detailFavActivity)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.favList)
    }

    override fun onDestroy() {
        super.onDestroy()
        favHelper.close()
    }

    private fun loadFavMoviesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            progressBar.visibility = View.VISIBLE
            val defferedMovies = async(Dispatchers.IO) {
                val cursor = favHelper.queryMovies()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            progressBar.visibility = View.INVISIBLE
            val movies = defferedMovies.await()
            if(movies.size > 0){
                adapter.favList = movies
            } else {
                adapter.favList = ArrayList()
                Snackbar.make(rv_fav_movie, R.string.no_data, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

}
