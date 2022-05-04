package com.example.pokedex.viewmodel

import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.*
import com.example.pokedex.data.Pokemon
import com.example.pokedex.data.PokemonDao
import com.example.pokedex.data.SortingData
import com.example.pokedex.data.retrieved.PokemonDetails
import com.example.pokedex.data.retrieved.PokemonStats
import com.example.pokedex.network.DexApi
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.DecimalFormat

class DexViewModel(private val pokemonDao: PokemonDao) : ViewModel() {

    private val _sortingData = MutableLiveData(SortingData("", true, "nationalNum"))
    val sortingData: LiveData<SortingData> = _sortingData

    // Needs to take all 3 search parameters into account.
    val pokemonEntities: LiveData<List<Pokemon>> = _sortingData.switchMap {

        if (_sortingData.value?.sortBy == "nationalNum" &&
                _sortingData.value?.ascending == true) {
            pokemonDao.searchByNumAsc(_sortingData.value?.searchText)
        }

        else if (_sortingData.value?.sortBy == "nationalNum" &&
            _sortingData.value?.ascending == false) {
            pokemonDao.searchByNumDesc(_sortingData.value?.searchText)
        }

        else if (_sortingData.value?.sortBy == "name" &&
            _sortingData.value?.ascending == true) {
            pokemonDao.searchByNameAsc(_sortingData.value?.searchText)
        }

        else if (_sortingData.value?.sortBy == "name" &&
            _sortingData.value?.ascending == false) {
            pokemonDao.searchByNameDesc(_sortingData.value?.searchText)
        }

        else if (_sortingData.value?.sortBy == "hpStat" &&
            _sortingData.value?.ascending == true ) {
            pokemonDao.searchByHpAsc(_sortingData.value?.searchText)
        }

        else if (_sortingData.value?.sortBy == "hpStat" &&
            _sortingData.value?.ascending == false) {
            pokemonDao.searchByHpDesc(_sortingData.value?.searchText)
        }

        else if (_sortingData.value?.sortBy == "attackStat" &&
            _sortingData.value?.ascending == true) {
            pokemonDao.searchByAttackAsc(_sortingData.value?.searchText)
        }

        else if (_sortingData.value?.sortBy == "attackStat" &&
            _sortingData.value?.ascending == false) {
            pokemonDao.searchByAttackDesc(_sortingData.value?.searchText)
        }

        else if (_sortingData.value?.sortBy == "defenseStat" &&
            _sortingData.value?.ascending == true) {
            pokemonDao.searchByDefenseAsc(_sortingData.value?.searchText)
        }

        else if (_sortingData.value?.sortBy == "defenseStat" &&
            _sortingData.value?.ascending == false) {
            pokemonDao.searchByDefenseDesc(_sortingData.value?.searchText)
        }

        else if (_sortingData.value?.sortBy == "specialAttackStat" &&
            _sortingData.value?.ascending == true) {
            pokemonDao.searchBySpecialAttackAsc(_sortingData.value?.searchText)
        }

        else if (_sortingData.value?.sortBy == "specialAttackStat" &&
            _sortingData.value?.ascending == false) {
            pokemonDao.searchBySpecialAttackDesc(_sortingData.value?.searchText)
        }

        else if (_sortingData.value?.sortBy == "specialDefenseStat" &&
            _sortingData.value?.ascending == true) {
            pokemonDao.searchBySpecialDefenseAsc(_sortingData.value?.searchText)
        }

        else if (_sortingData.value?.sortBy == "specialDefenseStat" &&
            _sortingData.value?.ascending == false) {
            pokemonDao.searchBySpecialDefenseDesc(_sortingData.value?.searchText)
        }

        else if (_sortingData.value?.sortBy == "totalStats" &&
                _sortingData.value?.ascending == true) {
            pokemonDao.searchByTotalAsc(_sortingData.value?.searchText)
        }

        else if (_sortingData.value?.sortBy == "totalStats" &&
            _sortingData.value?.ascending == false) {
            pokemonDao.searchByTotalDesc(_sortingData.value?.searchText)
        }

        else if (_sortingData.value?.sortBy == "speedStat" &&
            _sortingData.value?.ascending == true) {
            pokemonDao.searchBySpeedAsc(_sortingData.value?.searchText)
        }

        else {
            pokemonDao.searchBySpeedDesc(_sortingData.value?.searchText)
        }

    }

    // Initiate Pokemon data retrieval.
    fun startRetrieval() {
        viewModelScope.launch {
            pokemonDao.deleteAll()
            getPokemonEntries()
        }
    }

    // Retrieves a list of pokemon in the national dex.
    private suspend fun getPokemonEntries() {
        val pokemonList = viewModelScope.async {
            DexApi.retrofitService.getPokemon().pokemonSpecificsEntries
        }

        for (pokemon in pokemonList.await()) {
            viewModelScope.launch {
                Log.i("national_list", pokemon.toString())
                gatherData(pokemon.entryNumber.toString())
            }

        }
    }

    // Retrieve data for an individual Pokemon.
    private suspend fun gatherData(number: String) {
        val details = viewModelScope.async { getDetails(number) }
        val stats = viewModelScope.async { getStats(number) }
        val languageDescription: Int = getLanguageDescription(details.await())
        val languageGenus: Int = getLanguageGenus(details.await())
        val height: Double = stats.await().height.toDouble().div(10)
        val weight: Double = stats.await().weight.toDouble().div(10)
        val ability1: String? = getAbility(stats.await(),1)
        val ability2: String? = getAbility(stats.await(), 2)
        val hiddenAbility: String? = getAbility(stats.await(), 3)
        val type1: String? = getType(stats.await(), 1)
        val type2: String? = getType(stats.await(), 2)
        val hpStat: Int = getStat(stats.await(), "hp")
        val attackStat: Int = getStat(stats.await(), "attack")
        val defenseStat: Int = getStat(stats.await(), "defense")
        val specialAttackStat: Int = getStat(stats.await(), "special-attack")
        val specialDefenseStat: Int = getStat(stats.await(), "special-defense")
        val speedStat: Int = getStat(stats.await(), "speed")

        // Pass the retrieved parameters to the entity constructor.
        addNewPokemon(
            details.await().pokedexNumbers[0].entryNumber,
            stats.await().name,
            type1,
            type2,
            details.await().flavorTextEntries[languageDescription].flavorText,
            details.await().pokedexNumbers[0].entryNumber,
            height,
            weight,
            details.await().genera[languageGenus].genus,
            ability1,
            ability2,
            hiddenAbility,
            hpStat,
            attackStat,
            defenseStat,
            specialAttackStat,
            specialDefenseStat,
            speedStat
        )

//        try {
//            addNewPokemon(
//                details.await().pokedexNumbers[0].entryNumber,
//                stats.await().name,
//                stats.await().types[0].type.name,
//                stats.await().types[1].type.name,
//                details.await().flavorTextEntries[languageDescription].flavorText,
//                details.await().pokedexNumbers[0].entryNumber,
//                height,
//                weight,
//                details.await().genera[languageGenus].genus
//            )
//        } catch (e: IndexOutOfBoundsException) {
//            addNewPokemon(
//                details.await().pokedexNumbers[0].entryNumber,
//                stats.await().name,
//                stats.await().types[0].type.name,
//                "blank",
//                details.await().flavorTextEntries[languageDescription].flavorText,
//                details.await().pokedexNumbers[0].entryNumber,
//                height,
//                weight,
//                details.await().genera[languageGenus].genus
//            )
//        }

        Log.i("complete", number)
    }

    // Gather details about the Pokemon.
    private suspend fun getDetails(name: String): PokemonDetails {
        return DexApi.retrofitService.getPokemonDetails(name)
    }

    // Gather specific stats about the Pokemon.
    private suspend fun getStats(name: String): PokemonStats {
        return DexApi.retrofitService.getPokemonStats(name)
    }

    // Uses parameters to retrieve a newPokemon entity
    private fun addNewPokemon(
        id: Int,
        pokemonName: String,
        type1: String?,
        type2: String?,
        description: String,
        nationalNum: Int,
        height: Double,
        weight: Double,
        genus: String,
        ability1: String?,
        ability2: String?,
        hiddenAbility: String?,
        hpStat: Int,
        attackStat: Int,
        defenseStat: Int,
        specialAttackStat: Int,
        specialDefenseStat: Int,
        speedStat: Int
    ) {
        val newPokemon = getNewPokemonEntry(
            id,
            pokemonName,
            type1,
            type2,
            description,
            nationalNum,
            height,
            weight,
            genus,
            ability1,
            ability2,
            hiddenAbility,
            hpStat,
            attackStat,
            defenseStat,
            specialAttackStat,
            specialDefenseStat,
            speedStat
        )
        insertPokemon(newPokemon)
    }

    // Constructs the pokemon entity.
    private fun getNewPokemonEntry(
        id: Int,
        pokemonName: String,
        type1: String?,
        type2: String?,
        description: String,
        nationalNum: Int,
        height: Double,
        weight: Double,
        genus: String,
        ability1: String?,
        ability2: String?,
        hiddenAbility: String?,
        hpStat: Int,
        attackStat: Int,
        defenseStat: Int,
        specialAttackStat: Int,
        specialDefenseStat: Int,
        speedStat: Int
    ): Pokemon {
        return Pokemon(
            id = id,
            pokemonName = pokemonName,
            type1 = type1,
            type2 = type2,
            description = description,
            nationalNum = nationalNum,
            height = height,
            weight = weight,
            genus = genus,
            ability1 = ability1,
            ability2 = ability2,
            hiddenAbility = hiddenAbility,
            hpStat = hpStat,
            attackStat = attackStat,
            defenseStat = defenseStat,
            specialAttackStat = specialAttackStat,
            specialDefenseStat = specialDefenseStat,
            speedStat = speedStat,
            totalStats = speedStat + specialDefenseStat + specialAttackStat + defenseStat + attackStat + hpStat
        )
    }

    // Store the pokemon entity in the database.
    private fun insertPokemon(pokemon: Pokemon) {
        viewModelScope.launch {
            pokemonDao.insert(pokemon)
        }
    }

    // Returns the index for the first English pokemon description of a Pokemon.
    private fun getLanguageDescription(details: PokemonDetails): Int {
        var x = 0
        while (true) {
            if (details.flavorTextEntries[x].language.name == "en") {
                return x
            } else {
                x += 1
            }
        }
    }

    private fun getLanguageGenus(details: PokemonDetails): Int {
        var x = 0
        while (true) {
            if (details.genera[x].language.name == "en") {
                return x
            } else {
                x += 1
            }
        }
    }

    // Method that returns a specific pokemon from the live data list of pokemon entities.
    fun getPokemonEntity(id: Int): Pokemon {
        return pokemonEntities.value!![id]
    }

    // Method that returns an ability or null.
    private fun getAbility(stats: PokemonStats, slot: Int): String? {
        var i = 0
        while (true) {
            when {
                i > stats.abilities.size - 1 -> return null
                stats.abilities[i].slot == slot -> return stats.abilities[i].ability.name
                else -> i += 1
            }
//            if (i > stats.abilities.size - 1) {
//                return null
//            } else if (stats.abilities[i].slot == slot) {
//                return stats.abilities[i].ability.name
//            } else {
//                i += 1
//            }
//            try {
//                if (stats.abilities[i].slot == slot) {
//                    return stats.abilities[i].ability.name
//                } else {
//                    i += 1
//                }
//            } catch (e: IndexOutOfBoundsException) {
//                return null
//            }
        }
    }

    // Method that returns a type or null.
    private fun getType(stats: PokemonStats, slot: Int): String? {
        var i = 0
        while (true) {
            when {
                i > stats.types.size - 1 -> return null
                stats.types[i].slot == slot -> return stats.types[i].type.name
                else -> i += 1
            }
//            try {
//                if (stats.types[i].slot == slot) {
//                    return stats.types[i].type.name
//                } else {
//                    i += 1
//                }
//            } catch (e: IndexOutOfBoundsException) {
//                return null
//            }
        }
    }

    // Method for returning a base stats of a pokemon.
    private fun getStat(stats: PokemonStats, name: String): Int {
        var i = 0
        while (true) {
            if (stats.stats[i].stat.name == name) {
                return stats.stats[i].baseStat
            } else {
                i += 1
            }
        }
    }

    // Determine stat percent.
    fun getPercent(stat: Int, totalStats: Int): String {
        val df = DecimalFormat("#.##")
        val percent = ((stat.toDouble() / totalStats.toDouble()) * 100)
        return df.format(percent)
    }

    // Edit searchText in sorting data.
    fun searchPokemon(searchString: String?) {
        _sortingData.value = _sortingData.value?.copy(searchText = searchString)
    }


    // Toggles ascending vs descending.
    fun orderDex(ascending: Boolean) {
//        nameDescending.value = nameDescending.value?.let { !it }
        if (ascending != _sortingData.value?.ascending) {
            _sortingData.value = _sortingData.value?.copy(ascending = ascending)
        }

    }

    // Changes order of the dex by stat.
    fun setOrder(stat: String) {
        if (stat != _sortingData.value?.sortBy) {
            _sortingData.value = _sortingData.value?.copy(sortBy = stat)
        }
    }
}

class DexViewModelFactory(private val pokemonDao: PokemonDao): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DexViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DexViewModel(pokemonDao) as T
        }
        throw IllegalAccessException("Unknown ViewModel class")
    }
}