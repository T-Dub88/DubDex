package com.dubproductions.pokedex.data

import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.PrimaryKey

// Uses the PokemonStats entity to retrieve information.
@Entity(tableName = "AlternateForms")
data class AlternateForm(
    @PrimaryKey
    val id: Int,

    val nationalNum: Int,

    // Use species to link back to the original form.
    val species: String,

    val name: String,

    @Nullable
    val type1: String?,

    @Nullable
    val type2: String?,

    val height: Double,

    val weight: Double,

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

    val totalStats: Int

)
