package com.example.pokedex.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(pokemon: Pokemon)

//    @Query("SELECT * FROM national ORDER BY nationalNum ASC")
//    fun getAllByNumsAscending(): LiveData<List<Pokemon>>

    @Query("DELETE FROM national")
    suspend fun deleteAll()

//    @Query("SELECT * FROM national WHERE nationalNum = :nationalNum")
//    suspend fun getPokemon(nationalNum: String): Pokemon

//    @Query("SELECT * FROM National WHERE name = :search OR nationalNum = :search ")
//    fun filterDexList(search: String): LiveData<List<Pokemon>>

//    @Query("SELECT * FROM NATIONAL ORDER BY name ASC")
//    fun getAllByNamesAscending(): LiveData<List<Pokemon>>

    // Sorting queries for user inputs.
    @Query(
        "SELECT * FROM National WHERE instr(name, :search) > 0 " +
                "OR instr(nationalNum, :search) > 0 ORDER BY nationalNum ASC"
    )
    fun searchByNumAsc(search: String?): LiveData<List<Pokemon>>

    @Query(
        "SELECT * FROM National WHERE instr(name, :search) > 0 " +
                "OR instr(nationalNum, :search) > 0 ORDER BY nationalNum DESC"
    )
    fun searchByNumDesc(search: String?): LiveData<List<Pokemon>>

    @Query(
        "SELECT * FROM National WHERE instr(name, :search) > 0 " +
                "OR instr(nationalNum, :search) > 0 ORDER BY name ASC"
    )
    fun searchByNameAsc(search: String?): LiveData<List<Pokemon>>

    @Query(
        "SELECT * FROM National WHERE instr(name, :search) > 0 " +
                "OR instr(nationalNum, :search) > 0 ORDER BY name DESC"
    )
    fun searchByNameDesc(search: String?): LiveData<List<Pokemon>>

    @Query(
        "SELECT * FROM National WHERE instr(name, :search) > 0 " +
                "OR instr(nationalNum, :search) > 0 ORDER BY hpStat ASC"
    )
    fun searchByHpAsc(search: String?): LiveData<List<Pokemon>>

    @Query(
        "SELECT * FROM National WHERE instr(name, :search) > 0 " +
                "OR instr(nationalNum, :search) > 0 ORDER BY hpStat DESC"
    )
    fun searchByHpDesc(search: String?): LiveData<List<Pokemon>>

    @Query(
        "SELECT * FROM National WHERE instr(name, :search) > 0 " +
                "OR instr(nationalNum, :search) > 0 ORDER BY attackStat ASC"
    )
    fun searchByAttackAsc(search: String?): LiveData<List<Pokemon>>

    @Query(
        "SELECT * FROM National WHERE instr(name, :search) > 0 " +
                "OR instr(nationalNum, :search) > 0 ORDER BY attackStat DESC"
    )
    fun searchByAttackDesc(search: String?): LiveData<List<Pokemon>>

    @Query(
        "SELECT * FROM National WHERE instr(name, :search) > 0 " +
                "OR instr(nationalNum, :search) > 0 ORDER BY defenseStat ASC"
    )
    fun searchByDefenseAsc(search: String?): LiveData<List<Pokemon>>

    @Query(
        "SELECT * FROM National WHERE instr(name, :search) > 0 " +
                "OR instr(nationalNum, :search) > 0 ORDER BY defenseStat DESC"
    )
    fun searchByDefenseDesc(search: String?): LiveData<List<Pokemon>>

    @Query(
        "SELECT * FROM National WHERE instr(name, :search) > 0 " +
                "OR instr(nationalNum, :search) > 0 ORDER BY specialAttackStat ASC"
    )
    fun searchBySpecialAttackAsc(search: String?): LiveData<List<Pokemon>>

    @Query(
        "SELECT * FROM National WHERE instr(name, :search) > 0 " +
                "OR instr(nationalNum, :search) > 0 ORDER BY specialAttackStat DESC"
    )
    fun searchBySpecialAttackDesc(search: String?): LiveData<List<Pokemon>>

    @Query(
        "SELECT * FROM National WHERE instr(name, :search) > 0 " +
                "OR instr(nationalNum, :search) > 0 ORDER BY specialDefenseStat ASC"
    )
    fun searchBySpecialDefenseAsc(search: String?): LiveData<List<Pokemon>>

    @Query(
        "SELECT * FROM National WHERE instr(name, :search) > 0 " +
                "OR instr(nationalNum, :search) > 0 ORDER BY specialDefenseStat DESC"
    )
    fun searchBySpecialDefenseDesc(search: String?): LiveData<List<Pokemon>>

    @Query(
        "SELECT * FROM National WHERE instr(name, :search) > 0 " +
                "OR instr(nationalNum, :search) > 0 ORDER BY speedStat ASC"
    )
    fun searchBySpeedAsc(search: String?): LiveData<List<Pokemon>>

    @Query(
        "SELECT * FROM National WHERE instr(name, :search) > 0 " +
                "OR instr(nationalNum, :search) > 0 ORDER BY speedStat DESC"
    )
    fun searchBySpeedDesc(search: String?): LiveData<List<Pokemon>>

    @Query(
        "SELECT * FROM National WHERE instr(name, :search) > 0 " +
                "OR instr(nationalNum, :search) > 0 ORDER BY totalStats ASC"
    )
    fun searchByTotalAsc(search: String?): LiveData<List<Pokemon>>

    @Query(
        "SELECT * FROM National WHERE instr(name, :search) > 0 " +
                "OR instr(nationalNum, :search) > 0 ORDER BY totalStats DESC"
    )
    fun searchByTotalDesc(search: String?): LiveData<List<Pokemon>>

}