package com.example.pokedex

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.pokedex.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment

        navController = navHostFragment.navController

    }

    // TODO: Handle missing abilities/hidden abilities to be more aesthetically pleasing.

    // TODO: Add pokemon cries maybe?

    // TODO: Maybe add moves?

    // TODO: Rework database download timing.

    // TODO: Two pane layout for tablets.

    // TODO: Add checks for bad data from API.

    // TODO: Redo cool animation stuff.

}
