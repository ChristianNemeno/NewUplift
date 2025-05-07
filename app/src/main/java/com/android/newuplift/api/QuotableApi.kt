package com.android.newuplift.api

import com.android.newuplift.utility.Quote
import retrofit2.http.GET
import retrofit2.http.Query

//
//interface QuotableApi {
//    /**
//     * Fetches a single random quote from api.quotable.io/random
//     */
//    @GET("random")
//    suspend fun getRandomQuote(): Quote // Returns a single Quote object
//}



interface QuotableApi {
    @GET("api/quotes/random")
    suspend fun getRandomQuote(
        @Query("tags") tags: String,
        @Query("count") count: Int = 1
    ): Quote
}

// this ones random quote lng
//interface QuotableApi {
//    @GET("random")
//    suspend fun getRandomQuote() : Quote
//
//}