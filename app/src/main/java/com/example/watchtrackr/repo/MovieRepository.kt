package com.example.watchtrackr.repo

import android.content.Context
import com.example.watchtrackr.data.local.JsonStorage
import com.example.watchtrackr.data.models.Movie
import com.example.watchtrackr.data.models.MovieStatus
import com.example.watchtrackr.data.remote.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class MovieRepository(context: Context) {
    private val storage = JsonStorage(context)
    private val api = ApiService.create()
    private val apiKey = "YOUR_OMDB_API_KEY" // replace or move to BuildConfig

    suspend fun loadAllLists(): Triple<List<Movie>, List<Movie>, List<Movie>> =
        withContext(Dispatchers.IO) {
            val watching = storage.loadWatching()
            val wishlist = storage.loadWishlist()
            val finished = storage.loadFinished()
            Triple(watching, wishlist, finished)
        }

    suspend fun addToList(listName: String, movie: Movie) {
        storage.addMovieToList(listName, movie.copy(status = when(listName){
            "watching" -> MovieStatus.WATCHING
            "finished" -> MovieStatus.FINISHED
            else -> MovieStatus.WISHLIST
        }))
    }

    suspend fun removeFromList(listName: String, movieId: String) {
        storage.removeMovieFromList(listName, movieId)
    }

    suspend fun searchRemote(query: String) = withContext(Dispatchers.IO) {
        val res = api.search(query, apiKey)
        res.Search ?: emptyList()
    }

    suspend fun getRemoteDetails(imdbId: String) = withContext(Dispatchers.IO) {
        api.getDetails(imdbId, "full", apiKey)
    }
}
