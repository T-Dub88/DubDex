package com.dubproductions.pokedex.data

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

    val type1: String?,

    val type2: String?,

    val height: Double,

    val weight: Double,

    val ability1: String?,

    val ability2: String?,

    val hiddenAbility: String?,

    val hpStat: Int,

    val attackStat: Int,

    val defenseStat: Int,

    val specialAttackStat: Int,

    val specialDefenseStat: Int,

    val speedStat: Int,

    val totalStats: Int

)
