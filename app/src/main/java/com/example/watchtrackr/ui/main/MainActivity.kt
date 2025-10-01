package com.example.watchtrackr.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.watchtrackr.R
import com.example.watchtrackr.databinding.ActivityMainBinding
import com.example.watchtrackr.ui.home.HomeFragment
import com.example.watchtrackr.ui.browse.BrowseActivity
import android.content.Intent

class MainActivity: AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Load HomeFragment by default
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, HomeFragment())
            .commit()

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    supportFragmentManager.beginTransaction().replace(R.id.container, HomeFragment()).commit()
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
