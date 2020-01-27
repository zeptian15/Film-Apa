package com.septian.filmapauiux.ui.favourite


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
import com.septian.filmapauiux.db.DataHelper
import com.septian.filmapauiux.db.DatabaseContract.NoteColumns.Companion.CONTENT_URI
import com.septian.filmapauiux.helper.MappingHelper
import com.septian.filmapauiux.model.Favourite
import kotlinx.android.synthetic.main.fragment_fav_tv_show.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class FavouritesTvShow : Fragment() {
    // Inisialisasi FavHelper
    private lateinit var favHelper: DataHelper
    private lateinit var recyclerAdapter: FavouriteRecyclerAdapter
    private lateinit var uriWithTvSHow: Uri

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
        private const val REQUEST_CODE = 100
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fav_tv_show, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favHelper = DataHelper.getInstance(view.context)
        favHelper.open()

        recyclerAdapter = FavouriteRecyclerAdapter()
        recyclerAdapter.notifyDataSetChanged()
        rv_fav_tv.layoutManager = LinearLayoutManager(context)
        rv_fav_tv.adapter = recyclerAdapter
        rv_fav_tv.setHasFixedSize(true)

        if (savedInstanceState == null) {
            // proses ambil data
            loadFavTvShowAsync()
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

        // Get All Tv Show
        uriWithTvSHow = Uri.parse("${CONTENT_URI}/tv")
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

    override fun onDestroy() {
        super.onDestroy()
        favHelper.close()
    }

    private fun loadFavTvShowAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            progressBar.visibility = View.VISIBLE
            val defferedMovies = async(Dispatchers.IO) {
                val cursor = activity?.contentResolver?.query(
                    uriWithTvSHow,
                    null,
                    null,
                    null,
                    null
                ) as Cursor
                MappingHelper.mapCursorToArrayList(cursor)
            }
            progressBar.visibility = View.INVISIBLE
            val tv = defferedMovies.await()
            if(tv.size > 0){
                recyclerAdapter.favList = tv
            } else {
                recyclerAdapter.favList = ArrayList()
                Snackbar.make(rv_fav_tv, R.string.no_data, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE) {
            if (resultCode == FavouriteDetail.RESULT_DELETE) {
                val pos = data?.getIntExtra(FavouriteDetail.EXTRA_POSITION, 0)
                recyclerAdapter.removeItem(pos)
                Snackbar.make(rv_fav_tv, R.string.success_del, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

}
