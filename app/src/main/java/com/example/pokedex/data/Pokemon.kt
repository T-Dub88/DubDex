package com.example.pokedex.data

import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "National")
data class Pokemon(
    @PrimaryKey()
    val id: Int,

    @ColumnInfo(name = "name")
    val pokemonName: String,

    @Nullable
    val type1: String?,

    @Nullable
    val type2: String?,

    val description: String,

    @ColumnInfo(name = "nationalNum")
    val nationalNum: Int,

    val height: Double,

    val weight: Double,

    val genus: String,

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
