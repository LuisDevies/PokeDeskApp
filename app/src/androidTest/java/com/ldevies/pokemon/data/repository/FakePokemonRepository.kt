package com.ldevies.pokemon.data.repository

import androidx.paging.PagingData
import com.ldevies.pokemon.data.local.entity.PokemonEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

class FakePokemonRepository : PokemonRepository {
    
    private val pokemonList = mutableListOf<PokemonEntity>()
    val pokemonFlow = MutableStateFlow<PagingData<PokemonEntity>>(PagingData.empty())

    fun emitPokemon(list: List<PokemonEntity>) {
        pokemonFlow.value = PagingData.from(list)
    }

    override fun getPokemonList(query: String?): Flow<PagingData<PokemonEntity>> = pokemonFlow

    override fun getPokemonById(id: Int): Flow<PokemonEntity?> = flowOf(null)

    override suspend fun refreshPokemonDetail(id: Int) {}
}