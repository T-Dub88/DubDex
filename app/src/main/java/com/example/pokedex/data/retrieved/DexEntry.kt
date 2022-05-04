package com.example.pokedex.data.retrieved

import com.squareup.moshi.Json

data class DexEntry(
    @Json(name = "pokemon_entries")
    val pokemonSpecificsEntries: List<PokemonSpecifics>

) {
    data class PokemonSpecifics(
        @Json(name = "entry_number")
        val entryNumber: Int,

        @Json(name = "pokemon_species")
        val pokemonSpecies: PokemonBasics
    ) {
        data class PokemonBasics(
            val name: String
        )
    }

}




