package com.android.newuplift.database

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.android.newuplift.utility.Quote
import com.android.newuplift.utility.UserAccount

/**
 * To do , continue adding authentication currently you have CRUD for favorite quotes
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
        private const val COLUMN_QUOTE = "quote"
        private const val COLUMN_AUTHOR = "author"
        private const val COLUMN_TIMESTAMP = "timestamp"
        private const val COLUMN_IS_FAVORITE = "is_favorite"
        private const val COLUMN_IS_USER_MADE = "is_user_made"
        private const val COLUMN_TAGS = "tags"
        private const val COLUMN_LENGTH = "length"
        private const val COLUMN_SOURCE = "source"
        private const val COLUMN_USER_ID = "user_id"  // Added to match schema

        private const val TABLE_NAME_USER = "users"
        private const val COLUMN_USER_PK = "u_id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_USER = "username"
        private const val COLUMN_PASSWORD = "password"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_ADDRESS = "address"
        private const val COLUMN_NUMBER = "number"
    }

    // Quote Operations - Now user-specific

    /**
     * the insertQuote accepts params quote : Quote , and userId: Int
     * the Quote generated has an existing key , and the userId is from the singleton authenticate
     * which tracks the id of the current user ( a funciton made to return the id in log in is made )
     */
    fun insertQuote(quote: Quote, userId: Int): Long {
        if (isQuoteExists(quote.id, userId)) {
            return -1L
        }
        val values = ContentValues().apply {
            put(COLUMN_ID, quote.id)
            put(COLUMN_QUOTE, quote.quote)
            put(COLUMN_AUTHOR, quote.author)
            put(COLUMN_TIMESTAMP, quote.timestamp)
            put(COLUMN_TAGS, quote.tags.joinToString(", "))
            put(COLUMN_IS_FAVORITE, if (quote.isFavorite) 1 else 0)
            put(COLUMN_IS_USER_MADE, if (quote.isUserMade) 1 else 0)
            put(COLUMN_LENGTH, quote.length)
            put(COLUMN_USER_ID, userId)  // Link quote to user
        }
        return db.insertWithOnConflict(
            TABLE_NAME,
            null,
            values,
            SQLiteDatabase.CONFLICT_IGNORE
        )
    }

    fun updateFavorite(id: Int, userId: Int, isFavorite: Boolean): Int {
        val values = ContentValues().apply {
            put(COLUMN_IS_FAVORITE, if (isFavorite) 1 else 0)
        }
        Log.d("QuoteDao", "Updating: table=$TABLE_NAME, id=$id, userId=$userId, isFavorite=$isFavorite")
        Log.d("QuoteDao", "Where clause: $COLUMN_ID = ? AND $COLUMN_USER_ID = ?, args=${id.toString()}, ${userId.toString()}")
        return db.update(
            TABLE_NAME,
            values,
            "$COLUMN_ID = ? AND $COLUMN_USER_ID = ?",
            arrayOf(id.toString(), userId.toString())
        )
    }

    fun isQuoteExists(id: Int, userId: Int): Boolean {
        val cursor: Cursor = db.query(
            TABLE_NAME,
            arrayOf(COLUMN_ID),
            "$COLUMN_ID = ? AND $COLUMN_USER_ID = ?",
            arrayOf(id.toString(), userId.toString()),
            null,
            null,
            null
        )
        cursor.use {
            return it.moveToFirst()
        }
    }

    fun deleteQuote(id: Int, userId: Int): Int {
        return db.delete(
            TABLE_NAME,
            "$COLUMN_ID = ? AND $COLUMN_USER_ID = ?",
            arrayOf(id.toString(), userId.toString())
        )
    }

    fun getFavoriteQuotes(userId: Int): List<Quote> {
        val quotes = mutableListOf<Quote>()
        val cursor = db.query(
            TABLE_NAME,
            null,
            "$COLUMN_IS_FAVORITE = ? AND $COLUMN_USER_ID = ?",
            arrayOf("1", userId.toString()),
            null,
            null,
            "$COLUMN_TIMESTAMP DESC"
        )
        cursor.use {
            while (it.moveToNext()) {
                val quote = Quote(
                    id = it.getInt(it.getColumnIndexOrThrow(COLUMN_ID)),
                    quote = it.getString(it.getColumnIndexOrThrow(COLUMN_QUOTE)),
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


    fun insertAccount(account: UserAccount): Long {
        val values = ContentValues().apply {
            put(COLUMN_NAME, account.name)
            put(COLUMN_USER, account.username)
            put(COLUMN_PASSWORD, account.password)
            put(COLUMN_EMAIL, account.email)
            put(COLUMN_ADDRESS, account.address)
            put(COLUMN_NUMBER, account.number)
        }
        return db.insert(TABLE_NAME_USER, null, values)
    }

    fun isUsernameExists(username: String): Boolean {
        val cursor: Cursor = db.rawQuery(
            "SELECT COUNT(*) FROM $TABLE_NAME_USER WHERE $COLUMN_USER = ?",
            arrayOf(username)
        )
        cursor.use {
            if (it.moveToFirst()) {
                return it.getInt(0) > 0
            }
            return false
        }
    }

    // Added basic authentication method
    /**
     * This is the function that returns the current row's id it helps
     * to track the current user
     */
    fun authenticateUser(username: String, password: String): Int? {
        val cursor: Cursor = db.query(
            TABLE_NAME_USER,
            arrayOf(COLUMN_USER_PK),
            "$COLUMN_USER = ? AND $COLUMN_PASSWORD = ?",
            arrayOf(username, password),
            null,
            null,
            null
        )
        cursor.use {
            if (it.moveToFirst()) {
                return it.getInt(it.getColumnIndexOrThrow(COLUMN_USER_PK))
            }
            return null
        }
    }



    //------------------------------------------------------------------------------------------------
    // user made
    fun getUserMadeQuotes(userId: Int): List<Quote> {
        val quotes = mutableListOf<Quote>()
        val cursor = db.query(
            TABLE_NAME,
            null,
            "$COLUMN_IS_USER_MADE = ? AND $COLUMN_USER_ID = ?",
            arrayOf("1", userId.toString()),
            null,
            null,
            "$COLUMN_TIMESTAMP DESC"
        )
        cursor.use {
            while (it.moveToNext()) {
                val quote = Quote(
                    id = it.getInt(it.getColumnIndexOrThrow(COLUMN_ID)),
                    quote = it.getString(it.getColumnIndexOrThrow(COLUMN_QUOTE)),
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
    fun updateQuote(quote: Quote, userId: Int): Int {
        val values = ContentValues().apply {
            put(COLUMN_QUOTE, quote.quote)
            put(COLUMN_TAGS, quote.tags.joinToString(", "))
            put(COLUMN_IS_FAVORITE, if (quote.isFavorite) 1 else 0)
            put(COLUMN_LENGTH, quote.quote.length)
        }
        try {
            return db.update(
                TABLE_NAME,
                values,
                "$COLUMN_ID = ? AND $COLUMN_USER_ID = ?",
                arrayOf(quote.id.toString(), userId.toString())
            )
        } catch (e: Exception) {
            Log.e("QuoteDao", "Error updating quote: ${e.message}")
            return 0
        }
    }

    // ------------------------------------------------------------------------------
    fun getUserDetails(userId: Int): UserAccount? {
        val cursor: Cursor = db.query(
            TABLE_NAME_USER,
            null,
            "$COLUMN_USER_PK = ?",
            arrayOf(userId.toString()),
            null,
            null,
            null
        )

        cursor.use {
            return if (it.moveToFirst()) {
                UserAccount(
                    name = it.getString(it.getColumnIndexOrThrow(COLUMN_NAME)),
                    username = it.getString(it.getColumnIndexOrThrow(COLUMN_USER)),
                    password = "", // Don't expose password
                    email = it.getString(it.getColumnIndexOrThrow(COLUMN_EMAIL)),
                    address = it.getString(it.getColumnIndexOrThrow(COLUMN_ADDRESS)),
                    number = it.getString(it.getColumnIndexOrThrow(COLUMN_NUMBER))
                )
            } else {
                null
            }
        }
    }

//------------------------------------------------------------------------------
fun updateUserDetails(userId: Int, updatedUser: UserAccount): Int {
    val values = ContentValues().apply {
        put("name", updatedUser.name)
        put("username", updatedUser.username)
        put("email", updatedUser.email)
        put("address", updatedUser.address)
        put("number", updatedUser.number)
    }
    return db.update("users", values, "u_id = ?", arrayOf(userId.toString()))
}

// ------------------------------------------------------------------------------
}