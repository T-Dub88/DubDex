package com.example.databasebuilder.data

import androidx.annotation.Nullable
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

    @Nullable
    val type1: String?,

    @Nullable
    val type2: String?,

    val description: String?,

    val nationalNum: Int,

    val height: Double,

    val weight: Double,

    val genus: String?,

    val isBaby: Boolean,

    @Nullable
    val ability1: String?,

    @Nullable
    val ability2: String?,

    @Nullable
    val hiddenAbility: String?,

    val hpStat: Int,

    val attackStat: Int,

    val defenseStat: Int,

    val specialAttackStat: Int,

    val specialDefenseStat: Int,

    val speedStat: Int,

    val totalStats: Int,

    @Nullable
    val evolvesFrom: String?,

    val evolutionChain: Int,

    @Nullable
    val evolutionTrigger: String?,

    @Nullable
    val gender: Int? = null,

    @Nullable
    val heldItem: String? = null,

    @Nullable
    val item: String? = null,

    @Nullable
    val knowMove: String? = null,

    @Nullable
    val knownMoveType: String? = null,

    @Nullable
    val location: String? = null,

    @Nullable
    val minAffection: Int? = null,

    @Nullable
    val minBeauty: Int? = null,

    @Nullable
    val minHappiness: Int? = null,

    @Nullable
    val minLevel: Int? = null,

    @Nullable
    val needsOverworldRain: Boolean? = false,

    @Nullable
    val partySpecies: String? = null,

    @Nullable
    val partyType: String? = null,

    @Nullable
    val relativePhysicalStats: Int? = null,

    @Nullable
    val timeOfDay: String? = "",

    @Nullable
    val tradeSpecies: String? = null,

    val turnUpsideDown: Boolean? = false
)
