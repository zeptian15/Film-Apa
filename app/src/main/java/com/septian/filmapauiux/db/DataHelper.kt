package com.septian.filmapauiux.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.septian.filmapauiux.db.DatabaseContract.NoteColumns.Companion.ID
import com.septian.filmapauiux.db.DatabaseContract.NoteColumns.Companion.TABLE_NAME
import com.septian.filmapauiux.db.DatabaseContract.NoteColumns.Companion.TYPE
import java.sql.SQLException

class DataHelper(context: Context) {
    private val databaseHelper: DatabaseHelper = DatabaseHelper(context)

    private lateinit var database: SQLiteDatabase

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private var INSTANCE: DataHelper? = null

        fun getInstance(context: Context): DataHelper {
            if(INSTANCE == null){
                synchronized(SQLiteOpenHelper::class.java){
                    if(INSTANCE == null){
                        INSTANCE = DataHelper(context)
                    }
                }
            }
            return INSTANCE as DataHelper
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
            "$ID ASC",
            null
        )
    }

    fun queryById(id: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "$ID = ?",
            arrayOf(id),
            null,
            null,
            null,
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
            "$ID ASC",
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
            "$ID ASC",
            null)
    }

    fun insert(values: ContentValues?) : Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun deleteById(id: String): Int {
        return database.delete(DATABASE_TABLE, "$ID = ?", arrayOf(id))
    }
}