package com.android.newuplift.utility


data class Quote(
    val id: Int,
    val quote: String,
    val author: String,
    val length: Int,
    val tags: List<String>,
    var isFavorite: Boolean = false,
    val isUserMade: Boolean = false,
    val timestamp: Long = 0L
)

data class UserAccount(
    val name : String,
    val username : String,
    val password : String,
    val email : String,
    val address : String,
    val number : String
)

enum class QuoteType {
    FAVORITE,
    USER_MADE
}