package com.example.pokedex.data.retrieved


import com.squareup.moshi.Json

data class PokemonDetails(
    @Json(name = "flavor_text_entries")
    val flavorTextEntries: List<FlavorText>,

    @Json(name = "pokedex_numbers")
    val pokedexNumbers: List<PokedexNumber>,

    val genera: List<Genera>

) {
    data class PokedexNumber(
        @Json(name = "entry_number")
        val entryNumber: Int,

        val pokedex: Region
    ) {
        data class Region(
            val name: String
        )
    }

    data class FlavorText(
        @Json(name = "flavor_text")
        val flavorText: String,

        val language: LanguageSettings
    ) {
        data class LanguageSettings(
            val name: String
        )
    }

    data class Genera(
        val genus: String,
        val language: LanguageSettings
    ) {
        data class LanguageSettings(
            val name: String
        )
    }
}
