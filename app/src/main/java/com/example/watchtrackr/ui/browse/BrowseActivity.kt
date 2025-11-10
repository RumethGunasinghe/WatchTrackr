package com.example.watchtrackr.ui.browse

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.watchtrackr.data.models.Movie
import com.example.watchtrackr.databinding.ActivityBrowseBinding
import com.example.watchtrackr.ui.adapters.MovieAdapter
import com.example.watchtrackr.ui.details.MovieDetailsActivity
import com.example.watchtrackr.ui.manual.ManualAddActivity
import com.example.watchtrackr.vm.MainViewModel

class BrowseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBrowseBinding
    private lateinit var vm: MainViewModel
    private lateinit var adapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBrowseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vm = ViewModelProvider(this).get(MainViewModel::class.java)

        // RecyclerView setup
        adapter = MovieAdapter(
            items = mutableListOf(),
            onClick = { movie -> openDetails(movie) },
            onRemove = { movie -> vm.removeFrom("all", movie.id) }
        )

        binding.rvMovies.layoutManager = GridLayoutManager(this, 2)
        binding.rvMovies.adapter = adapter

        // 🔹 Fetch random/default movies on startup
        val randomKeywords = listOf("Avengers", "Batman", "Star", "King", "Spider", "Love", "War", "Matrix", "Dark", "Future")
        val randomQuery = randomKeywords.random()
        doSearch(randomQuery)

        // 🔹 Optional: update search hint to show what’s being shown
        binding.searchBar.queryHint = "Search movies (showing \"$randomQuery\")"

        // SearchView listener
        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val q = query?.trim()
                if (!q.isNullOrEmpty()) {
                    doSearch(q)
                    binding.btnAddManually.visibility = android.view.View.GONE
                } else {
                    binding.btnAddManually.visibility = android.view.View.VISIBLE
                }
                return true
            }

            override fun onQueryTextChange(newText: String?) = false
        })

        // Sort button
        binding.btnSort.setOnClickListener {
            adapter.sortByTitle()
        }

        // Add manually button
        binding.btnAddManually.setOnClickListener {
            val intent = Intent(this, ManualAddActivity::class.java)
            startActivity(intent)
        }

        // Observe ViewModel results
        vm.searchResults.observe(this) { results ->
            adapter.setItems(results)
            binding.btnAddManually.visibility =
                if (results.isEmpty()) android.view.View.VISIBLE else android.view.View.GONE
        }

        vm.error.observe(this) {
            // Show a Toast or Snackbar for errors
        }
    }

    private fun doSearch(query: String) {
        vm.searchRemote(query)
    }

    private fun openDetails(movie: Movie) {
        val intent = Intent(this, MovieDetailsActivity::class.java)
        intent.putExtra("movie", movie)
        startActivity(intent)
    }
}
