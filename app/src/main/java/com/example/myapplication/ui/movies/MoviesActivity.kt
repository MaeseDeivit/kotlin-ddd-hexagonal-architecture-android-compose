package com.example.myapplication.ui.movies

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMoviesBinding

class MoviesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMoviesBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMoviesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        val navController = findNavController(R.id.nav_host_fragment_movies)
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.fragmentMovieList)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_movies)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}