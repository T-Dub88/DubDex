package com.example.pokedex.data

data class SortingData(
    val searchText: String,
    val ascending: Boolean,
    val sortBy: SortByEnum,
    var temporarySearch: String
) {
    enum class SortByEnum {
        NATIONAL_NUM,
        NAME,
        HP_STAT,
        ATTACK_STAT,
        DEFENSE_STAT,
        SPECIAL_ATTACK_STAT,
        SPECIAL_DEFENSE_STAT,
        SPEED_STAT,
        TOTAL_STATS
    }

}

