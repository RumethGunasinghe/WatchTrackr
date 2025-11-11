package com.example.watchtrackr.ui.details

import android.content.Intent
import android.os.Bundle
import android.view.View
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        vm = ViewModelProvider(this).get(MainViewModel::class.java)

        // Use the typed getParcelableExtra
        movie = intent.getParcelableExtra("movie", Movie::class.java)

        // Null check instead of let
        if (movie != null) {
            val m = movie!!

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

            binding.btnAddWatching.setOnClickListener { vm.addTo("watching", m) }
            binding.btnAddWishlist.setOnClickListener { vm.addTo("wishlist", m) }
            binding.btnAddFinished.setOnClickListener { vm.addTo("finished", m) }


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
}
