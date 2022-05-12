package com.example.pokedex

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
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

    // TODO: Remove hardcoded strings.

    // TODO: Fix the way the type images are selected.

    // TODO: Handle images loading better/differently. Store them if possible.

    // TODO: Handle missing abilities/hidden abilities to be more aesthetically pleasing.

    // TODO: Add indicator for sorted stat on the recycler view.

    // TODO: Add evolution chains.

    // TODO: Add alternate forms of pokemon.

    // TODO: Add pokemon cries maybe?

    // TODO: Maybe add moves?

    // TODO: Rework database download to only load on first startup.

    // TODO: Two pane layout for tablets.

}