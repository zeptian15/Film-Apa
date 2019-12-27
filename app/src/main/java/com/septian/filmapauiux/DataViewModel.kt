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
    companion object {
        private const val API_KEY = "a1585fb3c55c240f60c0575f5102cc9d"
    }

    // Inisialisasi List Movies dan TvShows
    val listMovies = MutableLiveData<ArrayList<Movie>>()
    val listTvShows = MutableLiveData<ArrayList<TvShow>>()

    internal fun setMovies() {
        // Request data dari API
        val currentLanguage = Locale.getDefault().language
        val client = AsyncHttpClient()
        val listMoviesItems = ArrayList<Movie>()
        val url =
            "https://api.themoviedb.org/3/discover/movie?api_key=$API_KEY&language=$currentLanguage"

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
                        movieItems.title = movie.getString("title")
                        if (movie.getString("overview") == "") {
                            movieItems.description =
                                "Maaf deskripsi tidak tersedia dalam bahasa anda"
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
                Log.d("onFailure : ", error!!.message.toString())
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
            "https://api.themoviedb.org/3/discover/tv?api_key=$API_KEY&language=$currentLanguage"

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
                        tvShowItems.title = tvShow.getString("name")
                        if (tvShow.getString("overview") == "") {
                            tvShowItems.description =
                                "Maaf deskripsi tidak tersedia dalam bahasa anda"
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
                Log.d("onFailure : ", error!!.message.toString())
            }
        })
    }

    internal fun getTvShows(): LiveData<ArrayList<TvShow>> {
        return listTvShows
    }
}