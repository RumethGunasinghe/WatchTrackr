package com.example.watchtrackr.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.watchtrackr.data.models.Movie
import com.example.watchtrackr.repo.MovieRepository
import kotlinx.coroutines.launch

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = MovieRepository(app.applicationContext)

    val watching = MutableLiveData<List<Movie>>(listOf())
    val wishlist = MutableLiveData<List<Movie>>(listOf())
    val finished = MutableLiveData<List<Movie>>(listOf())
    val searchResults = MutableLiveData<List<Movie>>(listOf())
    val loading = MutableLiveData<Boolean>(false)
    val error = MutableLiveData<String?>(null)

    fun loadAll() = viewModelScope.launch {
        loading.value = true
        try {
            val (w, wl, f) = repo.loadAllLists()
            watching.value = w
            wishlist.value = wl
            finished.value = f
            error.value = null
        } catch (e: Exception) {
            error.value = e.message
        } finally {
            loading.value = false
        }
    }

    fun addTo(listName: String, movie: Movie) = viewModelScope.launch {
        repo.addToList(listName, movie)
        loadAll()
    }

    fun removeFrom(listName: String, movieId: String) = viewModelScope.launch {
        repo.removeFromList(listName, movieId)
        loadAll()
    }

    // Remote search using OMDb
    fun searchRemote(query: String) = viewModelScope.launch {
        loading.value = true
        try {
            val results = repo.searchRemote(query)
            // convert OmdbMovie -> Movie
            val mapped = results.map { om ->
                Movie(om.imdbID, om.Title, om.Year, description = null, thumbnail = if (om.Poster != "N/A") om.Poster else null)
            }
            searchResults.value = mapped
            error.value = null
        } catch (e: Exception) {
            error.value = e.message
        } finally {
            loading.value = false
        }
    }
}
