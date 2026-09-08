package com.ldevies.pokemon.data.repository

import androidx.paging.PagingData
import com.ldevies.pokemon.data.local.entity.PokemonEntity
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    fun getPokemonList(query: String? = null): Flow<PagingData<PokemonEntity>>
    fun getPokemonById(id: Int): Flow<PokemonEntity?>
    suspend fun refreshPokemonDetail(id: Int)
}