package com.dubproductions.pokedex

import android.app.Application
import com.dubproductions.pokedex.data.PokemonRoomDatabase

class PokedexApplication : Application() {
    val database: PokemonRoomDatabase by lazy { PokemonRoomDatabase.getDatabase(this) }
}
