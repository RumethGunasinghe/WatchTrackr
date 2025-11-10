package com.example.watchtrackr.data.remote

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

data class OmdbSearchResult(val Search: List<OmdbMovie>?, val totalResults: String?, val Response: String)
data class OmdbMovie(val imdbID: String, val Title: String, val Year: String, val Poster: String)
data class OmdbMovieDetail(val imdbID: String, val Title: String, val Year: String, val Poster: String, val Plot: String, val Response: String)

interface ApiService {
    @GET("/")
    suspend fun search(@Query("s") query: String, @Query("apikey") apiKey: String): OmdbSearchResult

    @GET("/")
    suspend fun getDetails(@Query("i") imdbId: String, @Query("plot") plot: String = "short", @Query("apikey") apiKey: String): OmdbMovieDetail

    companion object {
        fun create(): ApiService {
            val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
            val client = OkHttpClient.Builder().addInterceptor(logger).build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://www.omdbapi.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}
