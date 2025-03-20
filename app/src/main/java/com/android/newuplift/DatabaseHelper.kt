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
        private const val COLUMN_QUOTE = "quote"
        private const val COLUMN_AUTHOR = "author"
        private const val COLUMN_TIMESTAMP = "timestamp"

        private const val COLUMN_IS_FAVORITE = "is_favorite"
        private const val COLUMN_IS_USER_MADE = "is_user_made"
        private const val COLUMN_TAGS = "tags"
        private const val COLUMN_SOURCE = "source"
        private  const val COLUMN_LENGTH = "length"

        //for user account table
        private const val TABLE_NAME_USER = "users"
        private const val COLUMN_USER_ID = "u_id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_USER = "username"
        private const val COLUMN_PASSWORD = "password"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_ADRESS = "adress"
        private const val COLUMN_NUMBER = "number"


    }
    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY ,
                $COLUMN_QUOTE TEXT NOT NULL,
                $COLUMN_AUTHOR TEXT ,
                $COLUMN_TIMESTAMP INTEGER NOT NULL,
                $COLUMN_IS_FAVORITE INTEGER DEFAULT 0,
                $COLUMN_IS_USER_MADE INTEGER DEFAULT 0,
                $COLUMN_LENGTH INTEGER,
                $COLUMN_TAGS TEXT, 
                $COLUMN_SOURCE TEXT, 
                UNIQUE($COLUMN_QUOTE, $COLUMN_AUTHOR) 
               
            )
            
        """.trimIndent()

        val createTableUser = """
            CREATE TABLE $TABLE_NAME_USER (
            $COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_NAME TEXT NOT NULL,
            $COLUMN_USER TEXT NOT NULL,
            $COLUMN_PASSWORD TEXT NOT NULL,
            $COLUMN_EMAIL TEXT NOT NULL,
            $COLUMN_ADRESS TEXT NOT NULL,
            $COLUMN_NUMBER TEXT
        )
    """.trimIndent()


        db?.execSQL(createTableUser)
        db?.execSQL(createTableQuery)


        //
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }


}
