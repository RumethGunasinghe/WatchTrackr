package com.example.watchtrackr.ui.list

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.watchtrackr.R
import com.example.watchtrackr.databinding.ActivityListBinding
import com.example.watchtrackr.ui.adapters.MovieAdapter
import com.example.watchtrackr.ui.browse.BrowseActivity
import com.example.watchtrackr.ui.details.MovieDetailsActivity
import com.example.watchtrackr.ui.home.HomeFragment
import com.example.watchtrackr.ui.main.MainActivity
import com.example.watchtrackr.vm.MainViewModel

class ListActivity: AppCompatActivity() {

    private lateinit var binding: ActivityListBinding
    private lateinit var vm: MainViewModel
    private lateinit var adapter: MovieAdapter
    private var fromHome: Boolean = false
    private var listName: String = "wishlist"

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vm = ViewModelProvider(this).get(MainViewModel::class.java)
        listName = intent.getStringExtra("listName") ?: "wishlist"
        fromHome = listName in listOf("wishlist", "watching", "finished")
        title = listName.capitalize()

        // RecyclerView setup
        adapter = MovieAdapter(
            mutableListOf(),
            onClick = { openDetails(it) },
            onRemove = { vm.removeFrom(listName, it.id) },
            showRemove = fromHome
        )
        binding.recycler.layoutManager = LinearLayoutManager(this)
        binding.recycler.adapter = adapter

        // Observe ViewModel lists
        when(listName) {
            "watching" -> vm.watching.observe(this) { adapter.setItems(it) }
            "wishlist" -> vm.wishlist.observe(this) { adapter.setItems(it) }
            "finished" -> vm.finished.observe(this) { adapter.setItems(it) }
        }

        vm.loadAll()

        // 🔹 BottomNavigationView listener
        binding.bottomNav.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
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

    private fun openDetails(movie: com.example.watchtrackr.data.models.Movie) {
        val intent = Intent(this, MovieDetailsActivity::class.java)
        intent.putExtra("movie", movie)
        startActivity(intent)
    }
}
