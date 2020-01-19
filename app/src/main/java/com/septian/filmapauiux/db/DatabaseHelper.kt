package com.septian.filmapauiux.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.septian.filmapauiux.db.DatabaseContract.NoteColumns
import com.septian.filmapauiux.db.DatabaseContract.NoteColumns.Companion.TABLE_NAME

internal class DatabaseHelper (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

    companion object {

        private const val DATABASE_NAME = "dbfilmapa"
        private const val DATABASE_VERSION = 1
        private val SQL_CREATE_TABLE_FAVOURITE = "CREATE TABLE $TABLE_NAME" +
                " (${NoteColumns.ID} TEXT PRIMARY KEY," +
                " ${NoteColumns.TITLE} TEXT NOT NULL, " +
                " ${NoteColumns.DESCRIPTION} TEXT NOT NULL, " +
                " ${NoteColumns.LANGUAGE} TEXT NOT NULL, " +
                " ${NoteColumns.VOTE} TEXT NOT NULL, " +
                " ${NoteColumns.POSTER} TEXT NOT NULL, " +
                " ${NoteColumns.BACKGROUND} TEXT NOT NULL, " +
                " ${NoteColumns.TYPE} TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_FAVOURITE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}