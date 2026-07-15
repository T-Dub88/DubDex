package com.dubproductions.databasebuilder.data.retrieved


import com.squareup.moshi.Json

data class PokemonDetails(
    @param:Json(name = "evolution_chain")
    val evolutionChain: EvolutionChainNumber,

    @param:Json(name = "evolves_from_species")
    val evolvesFrom: EvolvesFrom?,

    @param:Json(name = "flavor_text_entries")
    val flavorTextEntries: List<FlavorText>,

    @param:Json(name = "pokedex_numbers")
    val pokedexNumbers: List<PokedexNumber>,

    val genera: List<Genera>,

    @param:Json(name = "is_baby")
    val isBaby: Boolean,

    val varieties: List<Variety>,

    ) {
    data class Variety(
        @param:Json(name = "is_default")
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
        @param:Json(name = "entry_number")
        val entryNumber: Int,

        val pokedex: Region
    ) {
        data class Region(
            val name: String
        )
    }

    data class FlavorText(
        @param:Json(name = "flavor_text")
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
