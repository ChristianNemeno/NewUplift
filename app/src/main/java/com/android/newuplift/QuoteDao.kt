package com.android.newuplift

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

/**
 * To do , continue adding autheticanion currently you have CRUD for favorite quotes
 */

/**
 * Welcome to QuoteDAO class , basically data access object class
 * In here you do crud operations sa given DB , you can see sa constructor signature (right sa class name)
 * I can put this in DatabaseHelper but abstracting it here makes it organize and modular easier to handle
 * for future exceptions
 */
class QuoteDao(private val db: SQLiteDatabase) {

    companion object {
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

    // Insert a FavoriteQuote into the database
    fun insertQuote(quote: Quote): Long {
        if (isQuoteExists(quote.id)) {
            return -1L
        }
        val values = ContentValues().apply {
            put(COLUMN_ID, quote.id)
            put(COLUMN_CONTENT, quote.content)
            put(COLUMN_AUTHOR, quote.author)
            put(COLUMN_TIMESTAMP, quote.timestamp)
            put(COLUMN_TAGS, quote.tags.joinToString(", "))
            put(COLUMN_IS_FAVORITE, if(quote.isFavorite) 1 else 0)
            put(COLUMN_IS_USER_MADE, if(quote.isUserMade) 1 else 0)
        }

        return db.insertWithOnConflict(
            TABLE_NAME,
            null,
            values,
            SQLiteDatabase.CONFLICT_IGNORE
        )

    }
    fun updateFavorite(id : String , isFavorite : Boolean) : Int{
        val values = ContentValues().apply{
            put(COLUMN_IS_FAVORITE, if(isFavorite) 1 else 0)
        }

        return db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(id))
    }
    private fun isQuoteExists(id: String): Boolean {
        val cursor: Cursor = db.query(
            TABLE_NAME,
            arrayOf(COLUMN_ID),
            "$COLUMN_ID = ?",
            arrayOf(id),
            null,
            null,
            null
        )
        cursor.use {
            return it.moveToFirst()
        }
    }

    fun deleteQuote(id: String): Int {
        return db.delete(
            TABLE_NAME,
            "$COLUMN_ID = ?",
            arrayOf(id)
        )
    }

    /**
     *      To add query for all favorites mga filtering functionalities i guess
     */
}