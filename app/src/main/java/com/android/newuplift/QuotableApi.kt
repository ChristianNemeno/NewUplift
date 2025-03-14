package com.android.newuplift

import retrofit2.http.GET
import retrofit2.http.Query

interface QuotableApi {
    // what this is an annotation sa retrofit library
    // the JSON response is an array of objects and GSON nalng mu deserialize para mu match
    // sa atong data class , basically queries a quote according sa tags filters out etc.
    @GET("quotes/random")
    suspend fun getRandomQuote(@Query("tags") tags :String) : List<Quote>

}

// this ones random quote lng
//interface QuotableApi {
//    @GET("random")
//    suspend fun getRandomQuote() : Quote
//
//}