package com.septian.filmapauiux

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.septian.filmapauiux.db.DataHelper
import com.septian.filmapauiux.db.DatabaseContract.AUTHORITY
import com.septian.filmapauiux.db.DatabaseContract.NoteColumns.Companion.CONTENT_URI
import com.septian.filmapauiux.db.DatabaseContract.NoteColumns.Companion.TABLE_NAME

class DataProvider : ContentProvider() {

    companion object {
        private const val FAVOURITE = 1
        private const val FAVOURITE_ID = 2
        private const val FAVOURITE_MOVIE = 3
        private const val FAVOURITE_TV = 4
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var dataHelper: DataHelper
    }

    init {
        // Content:// com.septian.filmapauiux/data
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME, FAVOURITE)

        // Content:// com.septian.filmapauiux/data
        sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/#", FAVOURITE_ID)

        // Content:// com.septian.filmapauiux/movie
        sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/movie", FAVOURITE_MOVIE)

        // Content:// com.septian.filmapauiux/tv
        sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/tv", FAVOURITE_TV)
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int = when (FAVOURITE_ID) {
            sUriMatcher.match(uri) -> dataHelper.deleteById(uri.lastPathSegment.toString())
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return deleted
    }

    override fun getType(uri: Uri): String? {
        // Do Nothing
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when (FAVOURITE) {
            sUriMatcher.match(uri) -> dataHelper.insert(values)
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun onCreate(): Boolean {
        dataHelper = DataHelper.getInstance(context as Context)
        dataHelper.open()
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        val cursor: Cursor?
        when (sUriMatcher.match(uri)) {
            FAVOURITE -> cursor = dataHelper.queryAll()
            FAVOURITE_MOVIE -> cursor = dataHelper.queryMovies()
            FAVOURITE_TV -> cursor = dataHelper.queryTv()
            FAVOURITE_ID -> cursor = dataHelper.queryById(uri.lastPathSegment.toString())
            else -> cursor = null
        }
        return cursor
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return 0
    }
}
