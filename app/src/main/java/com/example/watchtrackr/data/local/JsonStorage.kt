package com.example.watchtrackr.data.local

import android.content.Context
import com.example.watchtrackr.data.models.Movie
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class JsonStorage(private val context: Context) {
    private val gson = Gson()
    private val dir = context.filesDir

    private fun fileFor(statusName: String): File {
        return File(dir, "$statusName.json")
    }

    suspend fun loadList(name: String): MutableList<Movie> = withContext(Dispatchers.IO) {
        val f = fileFor(name)
        if (!f.exists()) {
            f.writeText("[]")
        }
        val json = f.readText()
        val type = object : TypeToken<MutableList<Movie>>() {}.type
        gson.fromJson<MutableList<Movie>>(json, type) ?: mutableListOf()
    }

    suspend fun saveList(name: String, items: List<Movie>) = withContext(Dispatchers.IO) {
        val f = fileFor(name)
        val json = gson.toJson(items)
        f.writeText(json)
    }

    // convenience functions
    suspend fun loadWatching() = loadList("watching")
    suspend fun loadWishlist() = loadList("wishlist")
    suspend fun loadFinished() = loadList("finished")

    suspend fun saveWatching(list: List<Movie>) = saveList("watching", list)
    suspend fun saveWishlist(list: List<Movie>) = saveList("wishlist", list)
    suspend fun saveFinished(list: List<Movie>) = saveList("finished", list)

    suspend fun addMovieToList(name: String, movie: Movie) {
        val list = loadList(name)
        // prevent duplicates by id
        if (list.none { it.id == movie.id }) {
            list.add(0, movie) // add to top
            saveList(name, list)
        }
    }

    suspend fun removeMovieFromList(name: String, movieId: String) {
        val list = loadList(name)
        val updated = list.filter { it.id != movieId }
        saveList(name, updated)
    }

    suspend fun moveMovie(from: String, to: String, movieId: String) {
        val fromList = loadList(from)
        val movie = fromList.find { it.id == movieId } ?: return
        val newFrom = fromList.filter { it.id != movieId }
        saveList(from, newFrom)
        addMovieToList(to, movie.copy(status = when (to) {
            "watching" -> com.example.watchtrackr.data.models.MovieStatus.WATCHING
            "finished" -> com.example.watchtrackr.data.models.MovieStatus.FINISHED
            else -> com.example.watchtrackr.data.models.MovieStatus.WISHLIST
        }.let { movie.status }))
    }
}
