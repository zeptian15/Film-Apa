package com.septian.filmapauiux


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.septian.filmapauiux.model.Movie
import com.septian.rickymaulana.filmapa.adapter.MovieAdapter

/**
 * A simple [Fragment] subclass.
 */
class MoviesFragment : Fragment() {
    // Inisialisasi Variabel
    private val list = ArrayList<Movie>()
    private lateinit var rvMovie: RecyclerView

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
        list.addAll(getListMovies())
        showRecyclerList()

    }

    // Method GetListMovies
    private fun getListMovies(): ArrayList<Movie> {
        val dataTitle = resources.getStringArray(R.array.movies_title)
        val dataRelease = resources.getStringArray(R.array.movies_release_year)
        val dataDescription = resources.getStringArray(R.array.movies_description)
        val dataPoster = resources.obtainTypedArray(R.array.movies_poster)
        val dataBackground = resources.obtainTypedArray(R.array.movies_background)

        val listMovie = ArrayList<Movie>()
        for (position in dataTitle.indices) {
            val movie = Movie(
                dataTitle[position],
                dataRelease[position],
                dataDescription[position],
                dataPoster.getResourceId(position, -1),
                dataBackground.getResourceId(position, -1)
            )
            listMovie.add(movie)
        }
        return listMovie
    }

    // Menampilkan RecyclerView
    private fun showRecyclerList() {
        rvMovie.layoutManager = LinearLayoutManager(activity)
        val listMovieAdapter = MovieAdapter(list)
        rvMovie.adapter = listMovieAdapter

        listMovieAdapter.setOnItemClickCallback(object : MovieAdapter.OnItemClickCallback {
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

}
