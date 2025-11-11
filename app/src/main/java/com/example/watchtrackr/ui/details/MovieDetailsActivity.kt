package com.example.watchtrackr.ui.details

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.watchtrackr.R
import com.example.watchtrackr.databinding.ActivityMovieDetailsBinding
import com.example.watchtrackr.data.models.Movie
import com.example.watchtrackr.ui.browse.BrowseActivity
import com.example.watchtrackr.ui.home.HomeFragment
import com.example.watchtrackr.vm.MainViewModel

class MovieDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMovieDetailsBinding
    private lateinit var vm: MainViewModel
    private var movie: Movie? = null

    private val activeColor by lazy { getColor(R.color.cancelButtonColor) }
    private val buttonColor by lazy { getColor(R.color.buttonColor) }
    private val disabledColor by lazy { getColor(R.color.deactivatedbuttonColor) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        vm = ViewModelProvider(this).get(MainViewModel::class.java)

        movie = intent.getParcelableExtra("movie", Movie::class.java)

        movie?.let { m ->
            title = m.title
            binding.tvTitle.text = m.title
            binding.tvDesc.text = """
                Genre: Action, Adventure, Comedy

                Director: James Gunn

                Actors: Chris Pratt, Zoe Saldaña, Dave Bautista

                Plot: The Guardians struggle to keep together as a team while dealing with their personal family issues, notably Star-Lord's encounter with his father, the ambitious celestial being Ego.
            """.trimIndent()
            binding.tvYear.text = m.year ?: ""

            if (!m.thumbnail.isNullOrEmpty()) {
                Glide.with(this).load(m.thumbnail).into(binding.ivPoster)
            } else {
                binding.ivPoster.setImageResource(R.drawable.ic_movie_placeholder)
            }

            // Initialize buttons
            resetButtons()

            binding.btnAddWatching.setOnClickListener { handleButtonClick("watching", m) }
            binding.btnAddWishlist.setOnClickListener { handleButtonClick("wishlist", m) }
            binding.btnAddFinished.setOnClickListener { handleButtonClick("finished", m) }

            // Bottom navigation
            binding.bottomNav.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_home -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.container, HomeFragment()).commit()
                        true
                    }
                    R.id.nav_browse -> {
                        startActivity(Intent(this, BrowseActivity::class.java))
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun handleButtonClick(listName: String, movie: Movie) {
        val clickedButton = when (listName) {
            "watching" -> binding.btnAddWatching
            "wishlist" -> binding.btnAddWishlist
            "finished" -> binding.btnAddFinished
            else -> null
        } ?: return

        val otherButtons = listOf(binding.btnAddWatching, binding.btnAddWishlist, binding.btnAddFinished)
            .filter { it != clickedButton }

        if (clickedButton.text == "Cancel") {
            // Undo: remove from list and reset all buttons
            vm.removeFrom(listName, movie.id)
            resetButtons()
        } else {
            // Add to list, set this button to Cancel, disable others
            vm.addTo(listName, movie)

            clickedButton.text = "Cancel"
            clickedButton.setBackgroundColor(activeColor)

            otherButtons.forEach {
                it.isEnabled = false
                it.setBackgroundColor(disabledColor)
            }
        }
    }

    private fun resetButtons() {
        binding.btnAddWatching.apply {
            isEnabled = true
            text = "Add to Watching"
            setBackgroundColor(buttonColor)
        }
        binding.btnAddWishlist.apply {
            isEnabled = true
            text = "Add to Wishlist"
            setBackgroundColor(buttonColor)
        }
        binding.btnAddFinished.apply {
            isEnabled = true
            text = "Add to Finished"
            setBackgroundColor(buttonColor)
        }
    }
}
