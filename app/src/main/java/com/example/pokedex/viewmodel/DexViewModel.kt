package com.example.pokedex.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.*
import com.example.pokedex.data.Pokemon
import com.example.pokedex.data.PokemonDao
import com.example.pokedex.data.SortingData
import com.example.pokedex.data.SortingData.SortByEnum
import com.example.pokedex.data.retrieved.EvolvesTo
import com.example.pokedex.data.retrieved.PokemonDetails
import com.example.pokedex.data.retrieved.PokemonStats
import com.example.pokedex.network.DexApi
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.DecimalFormat

class DexViewModel(private val pokemonDao: PokemonDao) : ViewModel() {

    // Sorting data class used to set the appropriate display list based on user sort options.
    private val _sortingData = MutableLiveData(SortingData("", true, SortByEnum.NATIONAL_NUM, ""))
    val sortingData: LiveData<SortingData> = _sortingData

    // Used to set the appropriate list of evolutions.
    private val _evolutionChainNum = MutableLiveData(1)

    // Sets the proper evolution chain list based on the evolution chain num.
    val evolutionList: LiveData<List<Pokemon>> = _evolutionChainNum.switchMap {
        pokemonDao.getChainList(_evolutionChainNum.value)
    }

    // Sets the proper list of pokemon based on the sorting data values.
    val pokemonEntities: LiveData<List<Pokemon>> = _sortingData.switchMap {

        if (_sortingData.value?.sortBy == SortByEnum.NATIONAL_NUM &&
                _sortingData.value?.ascending == true) {
            pokemonDao.searchByNumAsc(_sortingData.value?.searchText)
        }

        else if (_sortingData.value?.sortBy == SortByEnum.NATIONAL_NUM &&
            _sortingData.value?.ascending == false) {
            pokemonDao.searchByNumDesc(_sortingData.value?.searchText)
        }

        else if (_sortingData.value?.sortBy == SortByEnum.NAME &&
            _sortingData.value?.ascending == true) {
            pokemonDao.searchByNameAsc(_sortingData.value?.searchText)
        }

        else if (_sortingData.value?.sortBy == SortByEnum.NAME &&
            _sortingData.value?.ascending == false) {
            pokemonDao.searchByNameDesc(_sortingData.value?.searchText)
        }

        else if (_sortingData.value?.sortBy == SortByEnum.HP_STAT &&
            _sortingData.value?.ascending == true ) {
            pokemonDao.searchByHpAsc(_sortingData.value?.searchText)
        }

        else if (_sortingData.value?.sortBy == SortByEnum.HP_STAT &&
            _sortingData.value?.ascending == false) {
            pokemonDao.searchByHpDesc(_sortingData.value?.searchText)
        }

        else if (_sortingData.value?.sortBy == SortByEnum.ATTACK_STAT &&
            _sortingData.value?.ascending == true) {
            pokemonDao.searchByAttackAsc(_sortingData.value?.searchText)
        }

        else if (_sortingData.value?.sortBy == SortByEnum.ATTACK_STAT &&
            _sortingData.value?.ascending == false) {
            pokemonDao.searchByAttackDesc(_sortingData.value?.searchText)
        }

        else if (_sortingData.value?.sortBy == SortByEnum.DEFENSE_STAT &&
            _sortingData.value?.ascending == true) {
            pokemonDao.searchByDefenseAsc(_sortingData.value?.searchText)
        }

        else if (_sortingData.value?.sortBy == SortByEnum.DEFENSE_STAT &&
            _sortingData.value?.ascending == false) {
            pokemonDao.searchByDefenseDesc(_sortingData.value?.searchText)
        }

        else if (_sortingData.value?.sortBy == SortByEnum.SPECIAL_ATTACK_STAT &&
            _sortingData.value?.ascending == true) {
            pokemonDao.searchBySpecialAttackAsc(_sortingData.value?.searchText)
        }

        else if (_sortingData.value?.sortBy == SortByEnum.SPECIAL_ATTACK_STAT &&
            _sortingData.value?.ascending == false) {
            pokemonDao.searchBySpecialAttackDesc(_sortingData.value?.searchText)
        }

        else if (_sortingData.value?.sortBy == SortByEnum.SPECIAL_DEFENSE_STAT &&
            _sortingData.value?.ascending == true) {
            pokemonDao.searchBySpecialDefenseAsc(_sortingData.value?.searchText)
        }

        else if (_sortingData.value?.sortBy == SortByEnum.SPECIAL_DEFENSE_STAT &&
            _sortingData.value?.ascending == false) {
            pokemonDao.searchBySpecialDefenseDesc(_sortingData.value?.searchText)
        }

        else if (_sortingData.value?.sortBy == SortByEnum.TOTAL_STATS &&
                _sortingData.value?.ascending == true) {
            pokemonDao.searchByTotalAsc(_sortingData.value?.searchText)
        }

        else if (_sortingData.value?.sortBy == SortByEnum.TOTAL_STATS &&
            _sortingData.value?.ascending == false) {
            pokemonDao.searchByTotalDesc(_sortingData.value?.searchText)
        }

        else if (_sortingData.value?.sortBy == SortByEnum.SPEED_STAT &&
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

    // Initializes a coroutine to launch the chain count async operation.
    fun initializeChainCount() {
        viewModelScope.launch {
            getChainCount()
        }
    }

    // Used to get the evolution chain data for use in populating the database.
    private suspend fun getChainCount() {
        // Returns the chain numbers in a list.
        val chainList = viewModelScope.async {
            pokemonDao.getChainNumberList()
        }

        // Get data for each chain number in the list.
        for (i in chainList.await()) {
            val chainData = viewModelScope.async { DexApi.retrofitService.getChainData(i) }
            // Passes data to the function to launch database population.
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
        val languageDescription = getLanguageDescription(details.await())?.flavorText
        val genus = getLanguageGenus(details.await())?.genus
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
        val evolutionTrigger: String? = null
        val evolutionChain: Int = setEvolutionChainNumber(details.await().evolutionChain.url)

        // Pass the retrieved parameters to the entity constructor.
        addNewPokemon(
            details.await().pokedexNumbers[0].entryNumber,
            stats.await().name,
            type1,
            type2,
            languageDescription,
            details.await().pokedexNumbers[0].entryNumber,
            height,
            weight,
            genus,
            details.await().isBaby,
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
        description: String?,
        nationalNum: Int,
        height: Double,
        weight: Double,
        genus: String?,
        isBaby: Boolean,
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
        evolutionTrigger: String?,
        evolutionChain: Int
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
            isBaby,
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
        description: String?,
        nationalNum: Int,
        height: Double,
        weight: Double,
        genus: String?,
        isBaby: Boolean,
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
        evolutionTrigger: String?,
        evolutionChain: Int
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
            isBaby = isBaby,
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
            evolutionChain = evolutionChain,
            evolutionTrigger = evolutionTrigger,
        )
    }

    // Store the pokemon entity in the database.
    private fun insertPokemon(pokemon: Pokemon) {
        viewModelScope.launch {
            pokemonDao.insert(pokemon)
        }
    }

    // Returns the index for the first English pokemon description of a Pokemon.
    // Now returns the flavor text object with the correct language.
    private fun getLanguageDescription(details: PokemonDetails): PokemonDetails.FlavorText? {
//        var x = 0
        return details.flavorTextEntries.find { it.language.name == "en" }
//        while (true) {
//            if (details.flavorTextEntries[x].language.name == "en") {
//                return x
//            } else {
//                x += 1
//            }
//        }
    }

    // Returns the index of the first English genera.
    // Now returns the 1st genera object with the correct language.
    private fun getLanguageGenus(details: PokemonDetails): PokemonDetails.Genera? {
        return details.genera.find { it.language.name == "en" }
//        var x = 0
//        while (true) {
//            if (details.genera[x].language.name == "en") {
//                return x
//            } else {
//                x += 1
//            }
//        }
    }

    // Method that returns a specific pokemon from the live data list of pokemon entities.
    fun getPokemonEntity(id: Int): Pokemon? {
        return pokemonEntities.value?.find { it.nationalNum == id }
    }

    fun getEvolutionEntity(id: Int): Pokemon? {
        return evolutionList.value?.find { it.nationalNum == id }
    }

    // Method that returns an ability or null.
    private fun getAbility(stats: PokemonStats, slot: Int): String? {
        return stats.abilities.find { it.slot == slot }?.ability?.name
//        var i = 0
//        while (true) {
//            when {
//                i > stats.abilities.size - 1 -> return null
//                stats.abilities[i].slot == slot -> return stats.abilities[i].ability.name
//                else -> i += 1
//            }
//        }
    }

    // Method that returns a type or null.
    private fun getType(stats: PokemonStats, slot: Int): String? {
        return stats.types.find { it.slot == slot }?.type?.name
//        var i = 0
//        while (true) {
//            when {
//                i > stats.types.size - 1 -> return null
//                stats.types[i].slot == slot -> return stats.types[i].type.name
//                else -> i += 1
//            }
//        }
    }

    // Method for returning a base stats of a pokemon.
    private fun getStat(stats: PokemonStats, name: String): Int {
        val baseStat = stats.stats.find { it.stat.name == name }

        return baseStat?.baseStat ?: 0
//        var i = 0
//        while (true) {
//            if (stats.stats[i].stat.name == name) {
//                return stats.stats[i].baseStat
//            } else {
//                i += 1
//            }
//        }
    }

    // Determine stat percent.
    fun getPercent(stat: Int, totalStats: Int): String {
        val df = DecimalFormat("#.##")
        val percent = ((stat.toDouble() / totalStats.toDouble()) * 100)
        return df.format(percent)
    }

    // Edit searchText in sorting data.
    fun searchPokemon(searchString: String) {
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
    fun setOrder(stat: SortByEnum) {
        if (stat != _sortingData.value?.sortBy) {
            _sortingData.value = _sortingData.value?.copy(sortBy = stat)
        }
    }

    // Sets the evolution chain number for each pokemon in the database.
    private fun setEvolutionChainNumber(url: String): Int {
        val uri: Uri = Uri.parse(url)
        return uri.lastPathSegment?.toInt() ?: 0
    }

    // Sets the evolution data for each pokemon in the database.
    private fun getEvolutionData(evolvesTo: EvolvesTo) {
        for (evolution in evolvesTo.evolvesTo) {
            getEvolutionData(evolution)
        }

        val species = evolvesTo.species.name

        val details = try {
             evolvesTo.evolutionDetails[0]
        } catch (e: IndexOutOfBoundsException) {
            null
        }

        viewModelScope.launch {
            val currentEntity = viewModelScope.async { pokemonDao.findEntity(species) }
            val newEntity = currentEntity.await().copy(
                evolutionTrigger = details?.trigger?.name,
                gender = details?.gender,
                heldItem = details?.heldItem?.name,
                item = details?.item?.name,
                knowMove = details?.knownMove?.name,
                knownMoveType = details?.knownMoveType?.name,
                location = details?.location?.name,
                minAffection = details?.minAffection,
                minBeauty = details?.minBeauty,
                minHappiness = details?.minHappiness,
                minLevel = details?.minLevel,
                needsOverworldRain = details?.needsOverworldRain ?: false,
                partySpecies = details?.partySpecies?.name,
                partyType = details?.partyType?.name,
                relativePhysicalStats = details?.relativePhysicalStats,
                timeOfDay = details?.timeOfDay ?: "",
                tradeSpecies = details?.tradeSpecies?.name,
                turnUpsideDown = details?.turnUpsideDown ?: false
            )
            pokemonDao.updatePokemonDatabase(newEntity)
            Log.i("evo", "${newEntity.pokemonName} evolution data added.")
        }
    }

    // Retrieves the evolution chain for use in pokemon info fragment.
    fun getEvolutionChain(chainNum: Int) {
        _evolutionChainNum.value = chainNum
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
