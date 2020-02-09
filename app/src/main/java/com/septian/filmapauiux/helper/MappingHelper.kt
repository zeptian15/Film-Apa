package com.septian.filmapauiux.helper

import android.database.Cursor
import com.septian.filmapauiux.db.DatabaseContract
import com.septian.filmapauiux.model.Favourite

object MappingHelper {

    fun mapCursorToArrayList(notesCursor: Cursor): ArrayList<Favourite>{
        val favlist = ArrayList<Favourite>()
        while(notesCursor.moveToNext()){
            val id =
                notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns.ID))
            val title = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns.TITLE))
            val description = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns.DESCRIPTION))
            val language = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns.LANGUAGE))
            val vote = notesCursor.getDouble(notesCursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns.VOTE))
            val poster = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns.POSTER))
            val background = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns.BACKGROUND))
            val type = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns.TYPE))

            favlist.add(Favourite(id,title,description, language, vote, poster, background, type))
        }

        return favlist
    }
}