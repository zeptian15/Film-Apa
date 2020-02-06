package com.septian.filmapauiux.ui.favourite


import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.septian.filmapauiux.R
import com.septian.filmapauiux.adapter.FavouriteRecyclerAdapter
import com.septian.filmapauiux.db.DatabaseContract.NoteColumns.Companion.CONTENT_URI
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
class FavouritesMovies : Fragment() {
    // Inisialisasi
    private lateinit var recyclerAdapter: FavouriteRecyclerAdapter
    private lateinit var uriWithMovie: Uri

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
        private const val REQUEST_CODE = 100
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

        recyclerAdapter = FavouriteRecyclerAdapter()
        recyclerAdapter.notifyDataSetChanged()
        rv_fav_movie.layoutManager = LinearLayoutManager(context)
        rv_fav_movie.adapter = recyclerAdapter
        rv_fav_movie.setHasFixedSize(true)

        if (savedInstanceState == null) {
            // proses ambil data
            loadFavMoviesAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<Favourite>(EXTRA_STATE)
            if (list != null) {
                recyclerAdapter.favList = list
            }
        }

        recyclerAdapter.setOnItemClickCallback(object :
            FavouriteRecyclerAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Favourite, position: Int) {
                moveToDetailActivity(data, position)
            }
        })

        // Get All Movies
        uriWithMovie = Uri.parse("$CONTENT_URI/movie")

    }

    // Pindah ke Activity Detail Movie
    private fun moveToDetailActivity(favourite: Favourite, position: Int) {
        val detailFavActivity = Intent(context, FavouriteDetail::class.java)
        detailFavActivity.putExtra(FavouriteDetail.EXTRA_FAVOURITE, favourite)
        detailFavActivity.putExtra(FavouriteDetail.EXTRA_POSITION, position)
        startActivityForResult(
            detailFavActivity,
            REQUEST_CODE
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, recyclerAdapter.favList)
    }

    @SuppressLint("Recycle")
    private fun loadFavMoviesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            val defferedMovies = async(Dispatchers.IO) {
                val cursor =
                    activity?.contentResolver?.query(uriWithMovie, null, null, null, null) as Cursor
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val movies = defferedMovies.await()
            if (movies.size >= 0) {
                recyclerAdapter.favList = movies
            } else {
                recyclerAdapter.favList = ArrayList()
                Snackbar.make(rv_fav_movie, R.string.no_data, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE) {
            if (resultCode == FavouriteDetail.RESULT_DELETE) {
                val pos = data?.getIntExtra(FavouriteDetail.EXTRA_POSITION, 0)
                recyclerAdapter.removeItem(pos)
                Snackbar.make(rv_fav_movie, R.string.success_del, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

}
