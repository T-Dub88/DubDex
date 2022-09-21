package com.dubproductions.databasebuilder.data.retrieved


import com.squareup.moshi.Json

data class PokemonDetails(
    @Json(name = "evolution_chain")
    val evolutionChain: EvolutionChainNumber,

    @Json(name = "evolves_from_species")
    val evolvesFrom: EvolvesFrom?,

    @Json(name = "flavor_text_entries")
    val flavorTextEntries: List<FlavorText>,

    @Json(name = "pokedex_numbers")
    val pokedexNumbers: List<PokedexNumber>,

    val genera: List<Genera>,

    @Json(name = "is_baby")
    val isBaby: Boolean,

    val varieties: List<Variety>,

    ) {
    data class Variety(
        @Json(name = "is_default")
        val isDefault: Boolean,

        val pokemon: Type,
    ) {
        data class Type(
            val name: String,

            val url: String
        )
    }

    data class EvolutionChainNumber(
        val url: String
    )

    data class EvolvesFrom(
        val name: String?
    )

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
