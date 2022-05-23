package com.example.pokedex.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.*
import com.example.pokedex.data.Pokemon
import com.example.pokedex.data.PokemonDao
import com.example.pokedex.data.SortingData
import com.example.pokedex.data.retrieved.*
import com.example.pokedex.network.DexApi
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.IndexOutOfBoundsException
import java.text.DecimalFormat

class DexViewModel(private val pokemonDao: PokemonDao) : ViewModel() {

    private val _sortingData = MutableLiveData(SortingData("", true, "nationalNum", ""))
    val sortingData: LiveData<SortingData> = _sortingData

    // Sets the proper list of pokemon based on the sorting data values.
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

    fun initializeChainCount() {
        viewModelScope.launch {
            getChainCount()
        }
    }

    private suspend fun getChainCount() {
        val chainList = viewModelScope.async {
            pokemonDao.getChainNumberList()
        }

        for (i in chainList.await()) {
            val chainData = viewModelScope.async { DexApi.retrofitService.getChainData(i) }
            viewModelScope.launch { getEvolutionData(chainData.await().chain) }

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
        val evolvesFrom: String? = details.await().evolvesFrom?.name

        // Todo: Write functions to link and retrieve this information:
        val evolutionDetails: String? = null
        val evolutionTrigger: String? = null
        val evolutionChain: Int = setEvolutionChainNumber(details.await().evolutionChain.url)

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
            speedStat,
            evolvesFrom,
            evolutionDetails,
            evolutionTrigger,
            evolutionChain
        )
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
        speedStat: Int,
        evolvesFrom: String?,
        evolutionDetails: String?,
        evolutionTrigger: String?,
        evolutionChain: Int?
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
            speedStat,
            evolvesFrom,
            evolutionDetails,
            evolutionTrigger,
            evolutionChain
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
        speedStat: Int,
        evolvesFrom: String?,
        evolutionDetails: String?,
        evolutionTrigger: String?,
        evolutionChain: Int?
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
            totalStats = speedStat + specialDefenseStat + specialAttackStat + defenseStat + attackStat + hpStat,
            evolvesFrom = evolvesFrom,
            evolutionDetails = evolutionDetails,
            evolutionTrigger = evolutionTrigger,
            evolutionChain = evolutionChain
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

    // Sets the evolution chain number for each pokemon in the database.
    private fun setEvolutionChainNumber(url: String): Int {
        val uri: Uri = Uri.parse(url)
        return uri.lastPathSegment?.toInt() ?: 0
    }

    // Sets the evolution data for each pokemon.
    private fun getEvolutionData(evolvesTo: EvolvesTo) {
        for (evolution in evolvesTo.evolvesTo) {
            getEvolutionData(evolution)
        }

        var evolutionTrigger: String? = null
        val species = evolvesTo.species.name
        var evolutionDetails: String? = null

        try {
            evolutionTrigger = evolvesTo.evolutionDetails[0].trigger.name
            evolutionDetails = when {
                evolvesTo.evolutionDetails[0].gender != null -> {
                    when (evolvesTo.evolutionDetails[0].gender) {
                        1 -> "Female"
                        2 -> "Male"
                        else -> "Genderless"
                    }
                }

                evolvesTo.evolutionDetails[0].heldItem != null -> {
                    "Held Item: ${evolvesTo.evolutionDetails[0].heldItem?.name}"
                }

                evolvesTo.evolutionDetails[0].item != null -> {
                    "Item: ${evolvesTo.evolutionDetails[0].item?.name}"
                }

                evolvesTo.evolutionDetails[0].knownMove != null -> {
                    "Know Move: ${evolvesTo.evolutionDetails[0].knownMove?.name}"
                }

                evolvesTo.evolutionDetails[0].knownMoveType != null -> {
                    "Know Move Type: ${evolvesTo.evolutionDetails[0].knownMoveType?.name}"
                }

                evolvesTo.evolutionDetails[0].location != null -> {
                    "Location: ${evolvesTo.evolutionDetails[0].location?.name}"
                }

                evolvesTo.evolutionDetails[0].minAffection != null -> {
                    "Affection: ${evolvesTo.evolutionDetails[0].minAffection}"
                }

                evolvesTo.evolutionDetails[0].minBeauty != null -> {
                    "Beauty: ${evolvesTo.evolutionDetails[0].minBeauty}"

                }

                evolvesTo.evolutionDetails[0].minHappiness != null -> {
                    "Happiness: ${evolvesTo.evolutionDetails[0].minHappiness}"

                }

                evolvesTo.evolutionDetails[0].minLevel != null -> {
                    "Level: ${evolvesTo.evolutionDetails[0].minLevel}"
                }

                evolvesTo.evolutionDetails[0].needsOverworldRain != false -> {
                    "While raining"
                }

                evolvesTo.evolutionDetails[0].partySpecies != null -> {
                    "In Party: ${evolvesTo.evolutionDetails[0].partySpecies?.name}"
                }

                evolvesTo.evolutionDetails[0].partyType != null -> {
                    "Party Type: ${evolvesTo.evolutionDetails[0].partyType?.name}"
                }

                evolvesTo.evolutionDetails[0].relativePhysicalStats != null -> {
                    when (evolvesTo.evolutionDetails[0].relativePhysicalStats) {
                        1 -> "Attack > Defense"
                        0 -> "Attack == Defense"
                        else -> "Attack < Defense"
                    }
                }

                evolvesTo.evolutionDetails[0].timeOfDay != "" -> {
                    "Time: ${evolvesTo.evolutionDetails[0].timeOfDay}"
                }

                evolvesTo.evolutionDetails[0].tradeSpecies != null -> {
                    "Trade: ${evolvesTo.evolutionDetails[0].tradeSpecies?.name}"
                }

                evolvesTo.evolutionDetails[0].turnUpsideDown != false -> {
                    "Turn Upside-Down"
                }

                else -> {
                    null
                }
            }
        } catch (e: IndexOutOfBoundsException) {
            Log.i("noevo", "${evolvesTo.species.name} is a basic")
        }



        viewModelScope.launch {
            val currentEntity = viewModelScope.async { pokemonDao.findEntity(species) }
            val newEntity = currentEntity.await().copy(
                evolutionDetails = evolutionDetails,
                evolutionTrigger = evolutionTrigger
            )
            pokemonDao.updatePokemonDatabase(newEntity)
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
