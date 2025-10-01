package com.example.watchtrackr.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    val id: String,               // unique id (api id or generated)
    val title: String,
    val year: String? = null,
    val description: String? = null,
    val thumbnail: String? = null, // url or local file uri
    var status: MovieStatus = MovieStatus.WISHLIST
) : Parcelable

enum class MovieStatus { WATCHING, WISHLIST, FINISHED }
