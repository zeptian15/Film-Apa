package com.septian.filmapauiux

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.septian.filmapauiux.model.Movie
import com.septian.filmapauiux.model.TvShow
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class DataViewModel : ViewModel() {
    // Inisialisasi API Key

    // Inisialisasi List Movies dan TvShows
    val listMovies = MutableLiveData<ArrayList<Movie>>()
    val listTvShows = MutableLiveData<ArrayList<TvShow>>()
    val titleMovie = MutableLiveData<String>()
    val titleTvShow = MutableLiveData<String>()

    fun setTitleMovie(titleMovieName: String) {
        titleMovie.value = titleMovieName
    }

    fun setTitleTvShow(titleTvShowName: String) {
        titleTvShow.value = titleTvShowName
    }

    internal fun setMovies() {
        // Request data dari API
        val currentLanguage = Locale.getDefault().language
        val client = AsyncHttpClient()
        val listMoviesItems = ArrayList<Movie>()
        val url =
            "https://api.themoviedb.org/3/discover/movie?api_key=${BuildConfig.API_KEY}&language=$currentLanguage"

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                try {
                    val result = String(responseBody!!)
                    val responseObject = JSONObject(result)
                    var list = responseObject.getJSONArray("results")

                    for (i in 0 until list.length()) {
                        val movie = list.getJSONObject(i)
                        val movieItems = Movie()
                        movieItems.id = movie.getInt("id").toString()
                        movieItems.title = movie.getString("title")
                        if (movie.getString("overview") == "") {
                            movieItems.description =
                                "Maaf deskripsi tidak tersedia"
                        } else {
                            movieItems.description = movie.getString("overview")
                        }
                        movieItems.vote = movie.getDouble("vote_average")
                        movieItems.language = movie.getString("original_language")
                        movieItems.poster = movie.getString("poster_path")
                        movieItems.background = movie.getString("backdrop_path")

                        listMoviesItems.add(movieItems)
                    }
                    listMovies.postValue(listMoviesItems)
                } catch (e: Exception) {
                    Log.d("Exception : ", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                if (error != null) {
                    Log.d("onFailure : ", error.message.toString())
                }
            }
        })
    }

    internal fun getMovies(): LiveData<ArrayList<Movie>> {
        return listMovies
    }

    internal fun setTvShows() {
        // Request data dari API
        val client = AsyncHttpClient()
        val currentLanguage = Locale.getDefault().language
        val listTvShowsItems = ArrayList<TvShow>()
        val url =
            "https://api.themoviedb.org/3/discover/tv?api_key=${BuildConfig.API_KEY}&language=$currentLanguage"

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                try {
                    val result = String(responseBody!!)
                    val responseObject = JSONObject(result)
                    var list = responseObject.getJSONArray("results")

                    for (i in 0 until list.length()) {
                        val tvShow = list.getJSONObject(i)
                        val tvShowItems = TvShow()
                        tvShowItems.id = tvShow.getInt("id").toString()
                        tvShowItems.title = tvShow.getString("name")
                        if (tvShow.getString("overview") == "") {
                            tvShowItems.description =
                                "Maaf deskripsi tidak tersedia"
                        } else {
                            tvShowItems.description = tvShow.getString("overview")
                        }
                        tvShowItems.vote = tvShow.getDouble("vote_average")
                        tvShowItems.language = tvShow.getString("original_language")
                        tvShowItems.poster = tvShow.getString("poster_path")
                        tvShowItems.background = tvShow.getString("backdrop_path")

                        listTvShowsItems.add(tvShowItems)
                    }
                    listTvShows.postValue(listTvShowsItems)
                } catch (e: Exception) {
                    Log.d("Exception : ", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                if (error != null) {
                    Log.d("onFailure : ", error.message.toString())
                }
            }
        })
    }

    internal fun getTvShows(): LiveData<ArrayList<TvShow>> {
        return listTvShows
    }

    internal fun setSearchMovies(moviesName: String?) {
        // Request data dari API
        val currentLanguage = Locale.getDefault().language
        val client = AsyncHttpClient()
        val listMoviesItems = ArrayList<Movie>()
        val url =
            "https://api.themoviedb.org/3/search/movie?api_key=${BuildConfig.API_KEY}&language=$currentLanguage&query=$moviesName"

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                try {
                    val result = String(responseBody!!)
                    val responseObject = JSONObject(result)
                    var list = responseObject.getJSONArray("results")

                    for (i in 0 until list.length()) {
                        val movie = list.getJSONObject(i)
                        val movieItems = Movie()
                        movieItems.id = movie.getInt("id").toString()
                        movieItems.title = movie.getString("title")
                        if (movie.getString("overview") == "") {
                            movieItems.description =
                                "Maaf deskripsi tidak tersedia"
                        } else {
                            movieItems.description = movie.getString("overview")
                        }
                        movieItems.vote = movie.getDouble("vote_average")
                        movieItems.language = movie.getString("original_language")
                        movieItems.poster = movie.getString("poster_path")
                        movieItems.background = movie.getString("backdrop_path")

                        listMoviesItems.add(movieItems)
                    }
                    listMovies.postValue(listMoviesItems)
                } catch (e: Exception) {
                    Log.d("Exception : ", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                if (error != null) {
                    Log.d("onFailure : ", error.message.toString())
                }
            }
        })
    }

    internal fun getSearchMovies(): LiveData<ArrayList<Movie>> {
        return listMovies
    }

    internal fun setSearchTvShows(tvShowName: String?) {
        // Request data dari API
        val client = AsyncHttpClient()
        val currentLanguage = Locale.getDefault().language
        val listTvShowsItems = ArrayList<TvShow>()
        val url =
            "https://api.themoviedb.org/3/search/tv?api_key=${BuildConfig.API_KEY}&language=$currentLanguage&query=$tvShowName"

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                try {
                    val result = String(responseBody!!)
                    val responseObject = JSONObject(result)
                    var list = responseObject.getJSONArray("results")

                    for (i in 0 until list.length()) {
                        val tvShow = list.getJSONObject(i)
                        val tvShowItems = TvShow()
                        tvShowItems.id = tvShow.getInt("id").toString()
                        tvShowItems.title = tvShow.getString("name")
                        if (tvShow.getString("overview") == "") {
                            tvShowItems.description =
                                "Maaf deskripsi tidak tersedia"
                        } else {
                            tvShowItems.description = tvShow.getString("overview")
                        }
                        tvShowItems.vote = tvShow.getDouble("vote_average")
                        tvShowItems.language = tvShow.getString("original_language")
                        tvShowItems.poster = tvShow.getString("poster_path")
                        tvShowItems.background = tvShow.getString("backdrop_path")

                        listTvShowsItems.add(tvShowItems)
                    }
                    listTvShows.postValue(listTvShowsItems)
                } catch (e: Exception) {
                    Log.d("Exception : ", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                if (error != null) {
                    Log.d("onFailure : ", error.message.toString())
                }
            }
        })
    }

    internal fun getSearchTvShows(): LiveData<ArrayList<TvShow>> {
        return listTvShows
    }

}