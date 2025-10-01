package com.example.watchtrackr.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.watchtrackr.databinding.FragmentHomeBinding
import com.example.watchtrackr.ui.adapters.MovieAdapter
import com.example.watchtrackr.vm.MainViewModel
import android.content.Intent
import com.example.watchtrackr.ui.list.ListActivity
import com.example.watchtrackr.ui.details.MovieDetailsActivity

class HomeFragment: Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var vm: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        vm = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(v: View, s: Bundle?) {
        super.onViewCreated(v, s)
        val watchAdapter = MovieAdapter(mutableListOf(), onClick = { openDetails(it) }, onRemove = { vm.removeFrom("watching", it.id) })
        val wishAdapter = MovieAdapter(mutableListOf(), onClick = { openDetails(it) }, onRemove = { vm.removeFrom("wishlist", it.id) })
        val finAdapter  = MovieAdapter(mutableListOf(), onClick = { openDetails(it) }, onRemove = { vm.removeFrom("finished", it.id) })

        binding.rvWatching.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvWishlist.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvFinished.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.rvWatching.adapter = watchAdapter
        binding.rvWishlist.adapter = wishAdapter
        binding.rvFinished.adapter = finAdapter

        binding.btnSeeAllWatching.setOnClickListener { startList("watching") }
        binding.btnSeeAllWishlist.setOnClickListener { startList("wishlist") }
        binding.btnSeeAllFinished.setOnClickListener { startList("finished") }

        vm.watching.observe(viewLifecycleOwner) { list ->
            watchAdapter.setItems(list.take(3))
        }
        vm.wishlist.observe(viewLifecycleOwner) { list ->
            wishAdapter.setItems(list.take(3))
        }
        vm.finished.observe(viewLifecycleOwner) { list ->
            finAdapter.setItems(list.take(3))
        }

        vm.loadAll()
    }

    private fun startList(name: String){
        val intent = Intent(requireContext(), ListActivity::class.java)
        intent.putExtra("listName", name)
        startActivity(intent)
    }

    private fun openDetails(movie: com.example.watchtrackr.data.models.Movie) {
        val intent = Intent(requireContext(), MovieDetailsActivity::class.java)
        intent.putExtra("movie", movie)
        startActivity(intent)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
