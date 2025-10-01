package com.example.watchtrackr.ui.list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.watchtrackr.databinding.ActivityListBinding
import com.example.watchtrackr.ui.adapters.MovieAdapter
import com.example.watchtrackr.vm.MainViewModel
import android.content.Intent
import com.example.watchtrackr.ui.details.MovieDetailsActivity

class ListActivity: AppCompatActivity() {
    private lateinit var binding: ActivityListBinding
    private lateinit var vm: MainViewModel
    private lateinit var adapter: MovieAdapter
    private var listName: String = "wishlist"

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        vm = ViewModelProvider(this).get(MainViewModel::class.java)

        listName = intent.getStringExtra("listName") ?: "wishlist"
        title = listName.capitalize()

        adapter = MovieAdapter(mutableListOf(), onClick = { openDetails(it) }, onRemove = { vm.removeFrom(listName, it.id) })
        binding.recycler.layoutManager = LinearLayoutManager(this)
        binding.recycler.adapter = adapter

        when(listName) {
            "watching" -> vm.watching.observe(this) { adapter.setItems(it) }
            "wishlist" -> vm.wishlist.observe(this) { adapter.setItems(it) }
            "finished" -> vm.finished.observe(this) { adapter.setItems(it) }
        }

        vm.loadAll()
    }

    private fun openDetails(movie: com.example.watchtrackr.data.models.Movie) {
        val intent = Intent(this, MovieDetailsActivity::class.java)
        intent.putExtra("movie", movie)
        startActivity(intent)
    }
}
