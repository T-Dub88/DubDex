package com.example.pokedex.data

data class SortingData(
    val searchText: String?,
    val ascending: Boolean,
    val sortBy: String,
    var temporarySearch: String
)
