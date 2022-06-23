package com.example.pokedex.data.retrieved

import com.squareup.moshi.Json

data class PokemonStats(
    val abilities: List<Abilities>,
    val height: Int,
    val id: Int,
    val name: String,
    val species: Species,
    val stats: List<BaseStats>,
    val types: List<TypeData>,
    val weight: Int
) {
    data class Species(
        val name: String
    )

    data class BaseStats(
        @Json(name = "base_stat")
        val baseStat: Int,

        val stat: StatInfo
    ) {
        data class StatInfo(
            val name: String
        )
    }

    data class Abilities(
        val ability: Ability,

        @Json(name = "is_hidden")
        val hidden: Boolean,

        val slot: Int

    ) {
        data class Ability (
            val name: String
        )
    }

    data class TypeData(
        val slot: Int,
        val type: TypeSpecifics
    ) {
        data class TypeSpecifics(
            val name: String
        )
    }
}
