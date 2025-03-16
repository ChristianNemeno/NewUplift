package com.android.newuplift

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * To do continue , make table for accounts and do authetication in DAO dont do it here abstract ittt!
 */
class DatabaseHelper(context : Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private const val DATABASE_NAME =  "FavoriteQuotes"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "favorite_quotes"

        private const val COLUMN_ID = "id"
        private const val COLUMN_CONTENT = "content"
        private const val COLUMN_AUTHOR = "author"
        private const val COLUMN_TIMESTAMP = "timestamp"

        private const val COLUMN_IS_FAVORITE = "is_favorite"
        private const val COLUMN_IS_USER_MADE = "is_user_made"
        private const val COLUMN_TAGS = "tags"
        private const val COLUMN_SOURCE = "source"

    }
    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID TEXT PRIMARY KEY ,
                $COLUMN_CONTENT TEXT NOT NULL,
                $COLUMN_AUTHOR TEXT ,
                $COLUMN_TIMESTAMP INTEGER NOT NULL,
                $COLUMN_IS_FAVORITE INTEGER DEFAULT 0,
                $COLUMN_IS_USER_MADE INTEGER DEFAULT 0,
                $COLUMN_TAGS TEXT, 
                $COLUMN_SOURCE TEXT, 
                UNIQUE($COLUMN_CONTENT, $COLUMN_AUTHOR) 
            )
            
        """.trimIndent()

        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }


}
