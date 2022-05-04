package com.example.pokedex

import android.app.Application
import com.example.pokedex.data.PokemonRoomDatabase

class PokedexApplication : Application() {
    val database: PokemonRoomDatabase by lazy { PokemonRoomDatabase.getDatabase(this) }
}