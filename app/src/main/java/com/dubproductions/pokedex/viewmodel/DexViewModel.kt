package com.dubproductions.pokedex.viewmodel

import androidx.lifecycle.*
import com.dubproductions.pokedex.data.AlternateForm
import com.dubproductions.pokedex.data.Pokemon
import com.dubproductions.pokedex.data.PokemonDao
import com.dubproductions.pokedex.data.SortingData
import com.dubproductions.pokedex.data.SortingData.SortByEnum
import java.text.DecimalFormat

class DexViewModel(private val pokemonDao: PokemonDao) : ViewModel() {

    // Sorting data class used to set the appropriate display list based on user sort options.
    private val _sortingData = MutableLiveData(SortingData("", true, SortByEnum.NATIONAL_NUM, ""))
    val sortingData: LiveData<SortingData> = _sortingData

    // Used to set the appropriate list of evolutions.
    private val _evolutionChainNum = MutableLiveData(1)

    // Used to set the appropriate list of alternate forms.
    private val _species = MutableLiveData("")

    // Sets the proper evolution chain list based on the evolution chain num.
    val evolutionList: LiveData<List<Pokemon>> = _evolutionChainNum.switchMap {
        pokemonDao.getChainList(_evolutionChainNum.value)
    }

    // Sets the proper alternate form list based on the pokemon species.
    val alternateFormList: LiveData<List<AlternateForm>> = _species.switchMap {
        pokemonDao.getAlternateFormsList(_species.value)
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

    // Method that returns a specific pokemon from the live data list of pokemon entities.
    fun getPokemonEntity(id: Int): Pokemon? {
        return pokemonEntities.value?.find { it.nationalNum == id }
    }

    fun getEvolutionEntity(id: Int): Pokemon? {
        return evolutionList.value?.find { it.nationalNum == id }
    }

    fun getAlternateEntity(id: Int): AlternateForm? {
        return alternateFormList.value?.find { it.id == id }
    }

    fun getGenus(id: Int): String? {
        val previousForm: Pokemon? = evolutionList.value?.find { it.id == id }
        return previousForm?.genus
    }

    // Determine stat percent.
    fun getPercent(stat: Int, totalStats: Int): String {
        val df = DecimalFormat("#.##")
        val percent = ((stat.toDouble() / totalStats.toDouble()) * 100)
        return df.format(percent)
    }

    // Edit searchText in sorting data.
    fun searchPokemon(searchString: String) {
        _sortingData.value = _sortingData.value?.copy(searchText = searchString.lowercase())
    }

    // Toggles ascending vs descending.
    fun orderDex(ascending: Boolean) {
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

    // Retrieves the evolution chain for use in pokemon info fragment.
    fun getEvolutionChain(chainNum: Int) {
        _evolutionChainNum.value = chainNum
    }

    // Sets the species name for use in finding alternate forms in the database.
    fun getAlternateForms(speciesName: String) {
        _species.value = speciesName
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
