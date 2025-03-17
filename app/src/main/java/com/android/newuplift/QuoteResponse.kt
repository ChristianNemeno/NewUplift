package com.android.newuplift

import com.google.gson.annotations.SerializedName


data class Quote(
    @SerializedName("_id") val id : String,
    val content: String,
    val author: String,
    val timestamp: Long = System.currentTimeMillis(),
    val tags: List<String>,
    var isFavorite : Boolean = false,
    val isUserMade : Boolean = false
)

//data class FavoriteQuote(
//    val id : Long = 0,
//    val content: String,
//    val author: String,
//    val timestamp: Long = System.currentTimeMillis()
//)
//
