package com.septian.filmapauiux.widgets

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.AsyncTask
import android.os.Binder
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.septian.filmapauiux.R
import com.septian.filmapauiux.db.DataHelper
import com.septian.filmapauiux.db.DatabaseContract
import com.septian.filmapauiux.model.Favourite
import java.net.URL
import java.net.UnknownHostException


internal class FavouritesViewsFactory(private val context: Context) :
    RemoteViewsService.RemoteViewsFactory {
    private var bitmapImage: Bitmap? = null

    override fun onCreate() {}

    override fun getLoadingView(): RemoteViews? = null

    override fun getItemId(position: Int): Long = 0

    override fun onDataSetChanged() {
        val identityToken = Binder.clearCallingIdentity()
        Binder.restoreCallingIdentity(identityToken)
    }

    override fun hasStableIds(): Boolean = false

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun getViewAt(position: Int): RemoteViews {
        val remoteViews = RemoteViews(
            context.packageName,
            R.layout.widget_item
        )
        val baseUrl = "https://image.tmdb.org/t/p/w500"
        var widgetItems = mapCursorToArrayList()
        try {
            val bitmap: Bitmap = Glide.with(context)
                .asBitmap()
                .load(baseUrl + widgetItems[position].poster)
                .submit(512, 512)
                .get()
            remoteViews.setImageViewBitmap(R.id.imageView, bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("Opps", "Ada masalah nih")
        }
        val fillIntent = Intent()
        val extras = bundleOf(
            FavouritesWidget.EXTRA_ITEM to position,
            FavouritesWidget.EXTRA_NAME to widgetItems[position].title
        )
        fillIntent.putExtras(extras)
        remoteViews.setOnClickFillInIntent(R.id.imageView, fillIntent)
        return remoteViews
    }

    override fun getCount(): Int = mapCursorToArrayList().size

    override fun getViewTypeCount(): Int = 1

    override fun onDestroy() {}

    private fun mapCursorToArrayList(): ArrayList<Favourite> {
        val favHelper = DataHelper(context)
        favHelper.open()
        val cursor = favHelper.queryAll()
        val favouritesList = ArrayList<Favourite>()
        while (cursor.moveToNext()) {
            val id =
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns.ID))
            val title =
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns.TITLE))
            val description = cursor.getString(
                cursor.getColumnIndexOrThrow(
                    DatabaseContract.NoteColumns.DESCRIPTION
                )
            )
            val language =
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns.LANGUAGE))
            val vote =
                cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns.VOTE))
            val poster =
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns.POSTER))
            val background = cursor.getString(
                cursor.getColumnIndexOrThrow(
                    DatabaseContract.NoteColumns.BACKGROUND
                )
            )
            val type =
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns.TYPE))

            favouritesList.add(
                Favourite(
                    id,
                    title,
                    description,
                    language,
                    vote,
                    poster,
                    background,
                    type
                )
            )
        }
        return favouritesList
    }

    class ImageFromUrl(private val context: Context) : AsyncTask<URL, Nothing, Bitmap>() {
        private fun isConnected(context: Context): Boolean {
            var result = false
            val connManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            connManager?.run {
                connManager.getNetworkCapabilities(connManager.activeNetwork)?.run {
                    result = when {
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                        else -> false
                    }
                }
            }
            return result
        }

        override fun doInBackground(vararg params: URL?): Bitmap {
            val bitmap: Bitmap?
            val networkURL = params[0]
            bitmap = if (isConnected(context)) {
                try {
                    BitmapFactory.decodeStream(
                        networkURL?.openConnection()?.getInputStream()
                    )
                } catch (e: UnknownHostException) {
                    BitmapFactory.decodeResource(
                        context.resources,
                        R.drawable.transparent_gradient_bg
                    )
                }
            } else {
                BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.ic_dashboard_24dp
                )
            }
            return bitmap
        }

        fun getBitmap(url: String): Bitmap {
            return doInBackground(URL(url))
        }
    }

}