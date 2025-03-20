package com.android.newuplift.api


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {
    private const val BASE_URL = "https://quoteslate.vercel.app/"

    val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(QuotableApi::class.java)

}

// PLEASE DONT TOUCH I SPENT HOURS DEBUGGING THIS

// Basically expired na ang ceritifate ssl sa quotable last septempber and ang android
// di siya mu sugot kay time mismatch , handshake exception (i swear)
// so bypass nis ssl to work temporrarily


//object RetrofitClient {
//    private const val BASE_URL = "https://api.quotable.io/"
//
//    private val okHttpClient = OkHttpClient.Builder()
//        .sslSocketFactory(getUnsafeSSLSocketFactory(), TrustAllCerts())
//        .hostnameVerifier { _, _ -> true }
//        .build()
//
//    val api = Retrofit.Builder()
//        .baseUrl(BASE_URL)
//        .client(okHttpClient)
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()
//        .create(QuotableApi::class.java)
//
//    private fun getUnsafeSSLSocketFactory(): SSLSocketFactory {
//        val trustAllCerts = arrayOf<TrustManager>(TrustAllCerts())
//        val sslContext = SSLContext.getInstance("SSL")
//        sslContext.init(null, trustAllCerts, java.security.SecureRandom())
//        return sslContext.socketFactory
//    }
//}
//
//private class TrustAllCerts : X509TrustManager {
//    override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
//    override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
//    override fun getAcceptedIssuers(): Array<X509Certificate> = emptyArray()
//}