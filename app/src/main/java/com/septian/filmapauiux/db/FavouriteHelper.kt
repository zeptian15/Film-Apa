package com.septian.filmapauiux.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.septian.filmapauiux.db.DatabaseContract.NoteColumns.Companion.TABLE_NAME
import com.septian.filmapauiux.db.DatabaseContract.NoteColumns.Companion.TYPE
import com.septian.filmapauiux.db.DatabaseContract.NoteColumns.Companion._ID
import java.sql.SQLException

class FavouriteHelper (context: Context) {
    private val databaseHelper: DatabaseHelper = DatabaseHelper(context)

    private lateinit var database: SQLiteDatabase

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private var INSTANCE: FavouriteHelper? = null

        fun getInstance(context: Context): FavouriteHelper {
            if(INSTANCE == null){
                synchronized(SQLiteOpenHelper::class.java){
                    if(INSTANCE == null){
                        INSTANCE = FavouriteHelper(context)
                    }
                }
            }
            return INSTANCE as FavouriteHelper
        }
    }

    @Throws(SQLException::class)
    fun open() {
        database = databaseHelper.writableDatabase
    }

    fun close() {
        databaseHelper.close()

        if(database.isOpen)
            database.close()
    }

    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$_ID ASC",
            null)
    }

    fun queryMovies() : Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "$TYPE = 'movie'",
            null,
            null,
            null,
            "$_ID ASC",
            null)
    }

    fun queryTv() : Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "$TYPE = 'tv'",
            null,
            null,
            null,
            "$_ID ASC",
            null)
    }

    fun insert(values: ContentValues?) : Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun deleteById(id: String): Int {
        return database.delete(DATABASE_TABLE, "$_ID = ?", arrayOf(id))
    }
}