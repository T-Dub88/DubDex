package com.example.databasebuilder.data

import androidx.room.*

@Dao
interface DatabaseDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAlternate(pokemon: AlternateForm)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(pokemon: Pokemon)

    // Method for updating a row in the database.
    @Update
    suspend fun updatePokemonDatabase(pokemon: Pokemon)

    // Query for generating the list of distinct evolution chain numbers.
    @Query("SELECT DISTINCT evolutionChain FROM National")
    suspend fun getChainNumberList(): List<Int>
}
