package com.septian.filmapauiux.db

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    const val AUTHORITY = "com.septian.filmapauiux"
    const val SCHEME = "content"

    internal class NoteColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "favourite"
            const val ID = "id"
            const val TITLE = "title"
            const val DESCRIPTION = "description"
            const val LANGUAGE = "language"
            const val VOTE = "vote"
            const val POSTER = "poster"
            const val BACKGROUND = "background"
            const val TYPE = "type"

            // Buat URI Content
            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}