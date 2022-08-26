package com.example.pokedex.data.retrieved

import com.squareup.moshi.Json

data class EvolutionChain(
    val chain: EvolvesTo,
    val id: Int
)

//data class Chain(
//    @Json(name = "evolution_details")
//    val evolutionDetails: List<EvolutionDetails>,
//
//    @Json(name = "evolves_to")
//    val evolvesTo: List<EvolvesTo>,
//
//    )
data class EvolvesTo(
    @Json(name = "evolution_details")
    val evolutionDetails: List<EvolutionDetails>,

    @Json(name = "evolves_to")
    val evolvesTo: List<EvolvesTo>,

    @Json(name = "is_baby")
    val isBaby: Boolean,

    val species: Species
)

data class EvolutionDetails(
    val gender: Int?,

    @Json(name = "held_item")
    val heldItem: HeldItem?,

    val item: Item?,

    @Json(name = "known_move")
    val knownMove: KnownMove?,

    @Json(name = "known_move_type")
    val knownMoveType: KnownMoveType?,

    val location: Location?,

    @Json(name = "min_affection")
    val minAffection: Int?,

    @Json(name = "min_beauty")
    val minBeauty: Int?,

    @Json(name = "min_happiness")
    val minHappiness: Int?,

    @Json(name = "min_level")
    val minLevel: Int?,

    @Json(name = "needs_overworld_rain")
    val needsOverworldRain: Boolean?,

    @Json(name = "party_species")
    val partySpecies: PartySpecies?,

    @Json(name = "party_type")

    val partyType: PartyType?,

    // 1 Attack > Defense
    // 0 Attack == Defense
    // -1 Attack < Defense
    @Json(name = "relative_physical_stats")
    val relativePhysicalStats: Int?,

    @Json(name = "time_of_day")
    val timeOfDay: String?,

    @Json(name = "trade_species")
    val tradeSpecies: TradeSpecies?,

    val trigger: Trigger,

    @Json(name = "turn_upside_down")
    val turnUpsideDown: Boolean?,

    ) {
    data class HeldItem(
        val name: String
    )

    data class Item(
        val name: String
    )

    data class KnownMove(
        val name: String
    )

    data class KnownMoveType(
        val name: String
    )

    data class Location(
        val name: String
    )

    data class PartySpecies(
        val name: String
    )

    data class PartyType(
        val name: String
    )

    data class TradeSpecies(
        val name: String
    )

    data class Trigger(
        val name: String
    )
}

data class Species(
    val name: String
)
