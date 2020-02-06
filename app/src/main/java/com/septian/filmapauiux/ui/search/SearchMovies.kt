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
import com.septian.filmapauiux.adapter.MovieRecyclerAdapter
import com.septian.filmapauiux.model.Movie
import com.septian.filmapauiux.ui.detail.MovieDetailActivity
import kotlinx.android.synthetic.main.fragment_search_movies.*

/**
 * A simple [Fragment] subclass.
 */
class SearchMovies : Fragment() {
    private lateinit var rvMovie: RecyclerView
    private lateinit var recyclerAdapter: MovieRecyclerAdapter
    private lateinit var dataViewModel: DataViewModel
    private lateinit var skeletonSearchMovies: SkeletonScreen

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_movies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvMovie = view.findViewById(R.id.rv_movie)
        rvMovie.setHasFixedSize(true)

        recyclerAdapter = MovieRecyclerAdapter()
        recyclerAdapter.notifyDataSetChanged()
        rvMovie.layoutManager = LinearLayoutManager(context)
        rvMovie.adapter = recyclerAdapter

        // Inisialisasi DataViewModel
        dataViewModel = ViewModelProviders.of(activity!!).get(DataViewModel::class.java)

        dataViewModel.titleMovie.observe(viewLifecycleOwner, Observer { moviesName ->
            if (moviesName != null) {
                Snackbar.make(rv_movie, "Hasil : $moviesName", Snackbar.LENGTH_SHORT).show()
                dataViewModel.setSearchMovies(moviesName)
                skeletonSearchMovies =
                    Skeleton.bind(rvMovie).adapter(recyclerAdapter).load(R.layout.item_movie).show()
            }
        })

        dataViewModel.getSearchMovies().observe(viewLifecycleOwner, Observer { movieItems ->
            if (movieItems != null) {
                recyclerAdapter.setData(movieItems)
                skeletonSearchMovies.hide()
            }
        })
        recyclerAdapter.setOnItemClickCallback(object : MovieRecyclerAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Movie) {
                moveToDetailActivity(data)
            }
        })

    }

    // Pindah ke Activity Detail Movie
    private fun moveToDetailActivity(movie: Movie) {
        val detailMovieActivity = Intent(context, MovieDetailActivity::class.java)
        detailMovieActivity.putExtra(MovieDetailActivity.EXTRA_MOVIE, movie)
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
