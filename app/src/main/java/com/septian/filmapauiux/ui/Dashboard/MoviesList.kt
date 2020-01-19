package com.septian.filmapauiux.ui.Dashboard


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
import com.septian.filmapauiux.model.Movie
import com.septian.filmapauiux.ui.Detail.MovieDetail
import com.septian.rickymaulana.filmapa.adapter.MovieRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_movies.*

/**
 * A simple [Fragment] subclass.
 */
class MoviesList : Fragment() {
    // Inisialisasi Variabel
    private val list = ArrayList<Movie>()
    private lateinit var rvMovie: RecyclerView
    private lateinit var recyclerAdapter: MovieRecyclerAdapter

    private lateinit var dataViewModel: DataViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movies, container, false)
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
        dataViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(DataViewModel::class.java)

        // Masukan data ke dalam RecyclerView
        dataViewModel.setMovies()
        showLoading(true)

        dataViewModel.getMovies().observe(viewLifecycleOwner, Observer { movieItems ->
            if (movieItems != null) {
                recyclerAdapter.setData(movieItems)
                showLoading(false)
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
        val detailMovieActivity = Intent(context, MovieDetail::class.java)
        detailMovieActivity.putExtra(MovieDetail.EXTRA_MOVIE, movie)
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
