package com.dubproductions.databasebuilder.network

import com.dubproductions.databasebuilder.data.retrieved.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

private const val BASE_URL = "https://pokeapi.co/api/v2/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface DexApiService {
    @GET("pokedex/national")
    suspend fun getPokemon(): DexEntry

    @GET("pokemon-species/{pokemonNumber}")
    suspend fun getPokemonDetails(@Path("pokemonNumber") name: String): PokemonDetails

    @GET("pokemon/{pokemonNumber}")
    suspend fun getPokemonStats(@Path("pokemonNumber") name: String): PokemonStats

    @GET("evolution-chain/{chainNumber}")
    suspend fun getChainData(@Path("chainNumber") chainNumber: Int): EvolutionChain
}

object DexApi {
    val retrofitService : DexApiService by lazy {
        retrofit.create(DexApiService::class.java)
    }
}
