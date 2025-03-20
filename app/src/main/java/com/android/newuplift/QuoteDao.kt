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
        private const val COLUMN_QUOTE = "quote" // Changed from "content" to match QuoteSlate
        private const val COLUMN_AUTHOR = "author"
        private const val COLUMN_TIMESTAMP = "timestamp"
        private const val COLUMN_IS_FAVORITE = "is_favorite"
        private const val COLUMN_IS_USER_MADE = "is_user_made"
        private const val COLUMN_TAGS = "tags"
        private const val COLUMN_LENGTH = "length" // Added for QuoteSlate
        private const val COLUMN_SOURCE = "source"

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

    fun insertQuote(quote: Quote): Long {
        if (isQuoteExists(quote.id)) {
            return -1L
        }
        val values = ContentValues().apply {
            put(COLUMN_ID, quote.id)
            put(COLUMN_QUOTE, quote.quote) // Changed to quote
            put(COLUMN_AUTHOR, quote.author)
            put(COLUMN_TIMESTAMP, quote.timestamp)
            put(COLUMN_TAGS, quote.tags.joinToString(", "))
            put(COLUMN_IS_FAVORITE, if (quote.isFavorite) 1 else 0)
            put(COLUMN_IS_USER_MADE, if (quote.isUserMade) 1 else 0)
            put(COLUMN_LENGTH, quote.length) // Added length
        }
        return db.insertWithOnConflict(
            TABLE_NAME,
            null,
            values,
            SQLiteDatabase.CONFLICT_IGNORE
        )
    }

    fun updateFavorite(id: Int, isFavorite: Boolean): Int {
        val values = ContentValues().apply {
            put(COLUMN_IS_FAVORITE, if (isFavorite) 1 else 0)
        }
        return db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }
    fun isQuoteExists(id: Int): Boolean {
        val cursor: Cursor = db.query(
            TABLE_NAME,
            arrayOf(COLUMN_ID),
            "$COLUMN_ID = ?",
            arrayOf(id.toString()),
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

    fun getFavoriteQuotes(): List<Quote> {
        val quotes = mutableListOf<Quote>()
        val cursor = db.query(
            TABLE_NAME,
            null,
            "$COLUMN_IS_FAVORITE = ?",
            arrayOf("1"),
            null,
            null,
            "$COLUMN_TIMESTAMP DESC"
        )
        cursor.use {
            while (it.moveToNext()) {
                val quote = Quote(
                    id = it.getInt(it.getColumnIndexOrThrow(COLUMN_ID)), // Changed to Int
                    quote = it.getString(it.getColumnIndexOrThrow(COLUMN_QUOTE)), // Changed to quote
                    author = it.getString(it.getColumnIndexOrThrow(COLUMN_AUTHOR)) ?: "",
                    timestamp = it.getLong(it.getColumnIndexOrThrow(COLUMN_TIMESTAMP)),
                    tags = it.getString(it.getColumnIndexOrThrow(COLUMN_TAGS))?.split(", ") ?: emptyList(),
                    isFavorite = it.getInt(it.getColumnIndexOrThrow(COLUMN_IS_FAVORITE)) == 1,
                    isUserMade = it.getInt(it.getColumnIndexOrThrow(COLUMN_IS_USER_MADE)) == 1,
                    length = it.getInt(it.getColumnIndexOrThrow(COLUMN_LENGTH))
                )
                quotes.add(quote)
            }
        }
        return quotes
    }

    /**
     *  Code below , starts the functions for users aacount queries , use for various stuff
     *  e.g checking if username is taken ,
     */


    fun insertAccount(account : UserAccount): Long{
        val values = ContentValues().apply {
            put(COLUMN_NAME, account.name)
            put(COLUMN_USER, account.username)
            put(COLUMN_PASSWORD, account.password)
            put(COLUMN_EMAIL, account.email)
            put(COLUMN_ADRESS, account.adress)
            put(COLUMN_NUMBER, account.number)
        }

        return db.insert(TABLE_NAME_USER, null,values)
    }

    fun isUsernameExists(user : String) : Boolean{

        val cursor : Cursor = db.rawQuery(
            "SELECT COUNT(*) FROM $TABLE_NAME_USER  WHERE $COLUMN_USER =?",
            arrayOf(user)
        )

        cursor.use {

            if(it.moveToFirst()){
                return it.getInt(0) > 0
            }
            return false
        }
    }
    
}