package com.example.databasebuilder.viewmodel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.databasebuilder.data.AlternateForm
import com.example.databasebuilder.data.DataRoomDatabase
import com.example.databasebuilder.data.Pokemon
import com.example.databasebuilder.data.retrieved.EvolvesTo
import com.example.databasebuilder.data.retrieved.PokemonDetails
import com.example.databasebuilder.data.retrieved.PokemonStats
import com.example.databasebuilder.network.DexApi
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DatabaseViewModel(application: Application) : AndroidViewModel(application) {

    private val db = Room.databaseBuilder(
        application.applicationContext,
        DataRoomDatabase::class.java, "data_database"
    ).build()

    private val databaseDao = db.databaseDao()

    // Initiate Pokemon data retrieval.
    fun startRetrieval() {
        viewModelScope.launch {
            databaseDao.deleteAll()
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
            databaseDao.getChainNumberList()
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

    // Initialize adding alternate forms of pokemon.
    private fun launchFormsDownload(formsList: List<PokemonDetails.Variety>) {
        for (form in formsList) {
            if (!form.isDefault) {
                viewModelScope.launch {
                    val formNumber: Int = parseUrl(form.pokemon.url)
                    downloadAlternateData(formNumber)
                }
            }
        }
    }

    // Gather details about the Pokemon.
    private suspend fun getDetails(name: String): PokemonDetails {
        return DexApi.retrofitService.getPokemonDetails(name)
    }

    // Gather specific stats about the Pokemon.
    private suspend fun getStats(name: String): PokemonStats {
        return DexApi.retrofitService.getPokemonStats(name)
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
        val evolutionChain: Int = parseUrl(details.await().evolutionChain.url)

        // Pass the retrieved parameters to the entity constructor.
        addNewPokemon(
            details.await().pokedexNumbers[0].entryNumber,
            stats.await().species.name,
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

        if (details.await().varieties.size > 1) {
            launchFormsDownload(details.await().varieties)
        }
    }

    // Gathers data for passing to the construction of an alternate entity.
    private suspend fun downloadAlternateData(formNumber: Int) {
        val stats = viewModelScope.async { DexApi.retrofitService.getPokemonStats(formNumber.toString()) }
        val id: Int = stats.await().id
        val nationalNum: Int = parseUrl(stats.await().species.url)
        val species: String = stats.await().species.name
        val name: String = stats.await().name
        val type1: String? = getType(stats.await(), 1)
        val type2: String? = getType(stats.await(), 2)
        val height: Double = stats.await().height.toDouble().div(10)
        val weight: Double = stats.await().weight.toDouble().div(10)
        val ability1: String? = getAbility(stats.await(),1)
        val ability2: String? = getAbility(stats.await(), 2)
        val hiddenAbility: String? = getAbility(stats.await(), 3)
        val hpStat: Int = getStat(stats.await(), "hp")
        val attackStat: Int = getStat(stats.await(), "attack")
        val defenseStat: Int = getStat(stats.await(), "defense")
        val specialAttackStat: Int = getStat(stats.await(), "special-attack")
        val specialDefenseStat: Int = getStat(stats.await(), "special-defense")
        val speedStat: Int = getStat(stats.await(), "speed")

        addAlternateForm(
            id,
            nationalNum,
            species,
            name,
            type1,
            type2,
            height,
            weight,
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
    }

    // Uses parameters to pass new entry into the database.
    private fun addAlternateForm(
        id: Int,
        nationalNum: Int,
        species: String,
        name: String,
        type1: String?,
        type2: String?,
        height: Double,
        weight: Double,
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
        val newAlternateForm: AlternateForm = getNewFormEntity(
            id,
            nationalNum,
            species,
            name,
            type1,
            type2,
            height,
            weight,
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
        insertAlternateForm(newAlternateForm)
        Log.i("complete", id.toString())
    }

    // Uses parameters to retrieve a newPokemon entity
    private fun addNewPokemon(
        id: Int,
        species: String,
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
            species,
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
        Log.i("complete", id.toString())
    }

    // Constructs the alternate form data into an entity for the database.
    private fun getNewFormEntity(
        id: Int,
        nationalNum: Int,
        species: String,
        name: String,
        type1: String?,
        type2: String?,
        height: Double,
        weight: Double,
        ability1: String?,
        ability2: String?,
        hiddenAbility: String?,
        hpStat: Int,
        attackStat: Int,
        defenseStat: Int,
        specialAttackStat: Int,
        specialDefenseStat: Int,
        speedStat: Int
    ): AlternateForm {
        return AlternateForm(
            id = id,
            nationalNum = nationalNum,
            species = species,
            name = name,
            type1 = type1,
            type2 = type2,
            height = height,
            weight = weight,
            ability1 = ability1,
            ability2 = ability2,
            hiddenAbility = hiddenAbility,
            hpStat = hpStat,
            attackStat = attackStat,
            defenseStat = defenseStat,
            specialAttackStat = specialAttackStat,
            specialDefenseStat = specialDefenseStat,
            speedStat = speedStat,
            totalStats = hpStat + attackStat + defenseStat + specialAttackStat + specialDefenseStat + speedStat
        )
    }

    // Constructs the pokemon entity.
    private fun getNewPokemonEntry(
        id: Int,
        species: String,
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
            species = species,
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
            databaseDao.insert(pokemon)
        }
    }

    // Stores the alternate for entity into the database.
    private fun insertAlternateForm(pokemon: AlternateForm) {
        viewModelScope.launch {
            databaseDao.insertAlternate(pokemon)
        }
    }

    // Returns the index for the first English pokemon description of a Pokemon.
    // Now returns the flavor text object with the correct language.
    private fun getLanguageDescription(details: PokemonDetails): PokemonDetails.FlavorText? {
        return details.flavorTextEntries.find { it.language.name == "en" }

    }

    // Returns the index of the first English genera.
    // Now returns the 1st genera object with the correct language.
    private fun getLanguageGenus(details: PokemonDetails): PokemonDetails.Genera? {
        return details.genera.find { it.language.name == "en" }
    }

    // Method that returns an ability or null.
    private fun getAbility(stats: PokemonStats, slot: Int): String? {
        return stats.abilities.find { it.slot == slot }?.ability?.name
    }

    // Method that returns a type or null.
    private fun getType(stats: PokemonStats, slot: Int): String? {
        return stats.types.find { it.slot == slot }?.type?.name
    }

    // Method for returning a base stats of a pokemon.
    private fun getStat(stats: PokemonStats, name: String): Int {
        val baseStat = stats.stats.find { it.stat.name == name }
        return baseStat?.baseStat ?: 0
    }

    // Sets the evolution chain number for each pokemon in the database.
    private fun parseUrl(url: String): Int {
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
            val currentEntity = viewModelScope.async { databaseDao.findEntity(species) }
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
            databaseDao.updatePokemonDatabase(newEntity)
            Log.i("evo", "${newEntity.pokemonName} evolution data added.")
        }
    }

}
