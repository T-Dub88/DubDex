package com.dubproductions.pokedex.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "National")
data class Pokemon(
    @PrimaryKey
    val id: Int,

    // Used for linking to alternate forms.
    val species: String,

    @ColumnInfo(name = "name")
    val pokemonName: String,

    val type1: String?,

    val type2: String?,

    val description: String?,

    val nationalNum: Int,

    val height: Double,

    val weight: Double,

    val genus: String?,

    val isBaby: Boolean,

    val ability1: String?,

    val ability2: String?,

    val hiddenAbility: String?,

    val hpStat: Int,

    val attackStat: Int,

    val defenseStat: Int,

    val specialAttackStat: Int,

    val specialDefenseStat: Int,

    val speedStat: Int,

    val totalStats: Int,

    val evolvesFrom: String?,

    val evolutionChain: Int,

    val evolutionTrigger: String?,

    val gender: Int? = null,

    val heldItem: String? = null,

    val item: String? = null,

    val knowMove: String? = null,

    val knownMoveType: String? = null,

    val location: String? = null,

    val minAffection: Int? = null,

    val minBeauty: Int? = null,

    val minHappiness: Int? = null,

    val minLevel: Int? = null,

    val needsOverworldRain: Boolean? = false,

    val partySpecies: String? = null,

    val partyType: String? = null,

    val relativePhysicalStats: Int? = null,

    val timeOfDay: String? = "",

    val tradeSpecies: String? = null,

    val turnUpsideDown: Boolean? = false
)
