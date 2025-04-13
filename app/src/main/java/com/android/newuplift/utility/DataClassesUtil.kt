package com.android.newuplift.utility

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Quote(
    val id: Int,
    val quote: String,
    val author: String,
    val length: Int,
    val tags: List<String>,
    var isFavorite: Boolean = false,
    val isUserMade: Boolean = false,
    val timestamp: Long = 0L
) : Parcelable

data class UserAccount(
    val name: String,
    val username: String,
    val password: String,
    val email: String,
    val address: String,
    val number: String
)

enum class QuoteType {
    FAVORITE,
    USER_MADE
}