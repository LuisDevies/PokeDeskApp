package com.ldevies.pokemon.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ldevies.pokemon.data.local.PokemonDatabase
import com.ldevies.pokemon.data.local.entity.PokemonEntity
import com.ldevies.pokemon.data.paging.PokemonRemoteMediator
import com.ldevies.pokemon.data.remote.api.PokeApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PokemonRepositoryImpl @Inject constructor(
    private val apiService: PokeApiService,
    private val database: PokemonDatabase
) : PokemonRepository {

    private val dao = database.pokemonDao()

    @OptIn(ExperimentalPagingApi::class)
    override fun getPokemonList(query: String?): Flow<PagingData<PokemonEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 2,
                enablePlaceholders = false
            ),
            remoteMediator = if (query.isNullOrBlank()) {
                PokemonRemoteMediator(
                    database = database,
                    apiService = apiService
                )
            } else null,
            pagingSourceFactory = {
                if (query.isNullOrBlank()) {
                    dao.getAllPokemon()
                } else {
                    dao.searchPokemon(query)
                }
            }
        ).flow
    }

    override suspend fun refreshPokemonDetail(id: Int) {
        try {
            val detail = apiService.getPokemonDetail(id)
            val existing = PokemonEntity(
                id = detail.id,
                name = detail.name.replaceFirstChar { it.uppercase() },
                imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${detail.id}.png",
                height = detail.height,
                weight = detail.weight
            )
            dao.insertPokemon(existing)
        } catch (_: Exception) { }
    }

    override fun getPokemonById(id: Int): Flow<PokemonEntity?> = dao.getPokemonById(id)


}