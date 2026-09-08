package com.ldevies.pokemon.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.ldevies.pokemon.data.local.PokemonDatabase
import com.ldevies.pokemon.data.local.entity.PokemonEntity
import com.ldevies.pokemon.data.local.entity.RemoteKeyEntity
import com.ldevies.pokemon.data.remote.api.PokeApiService
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class PokemonRemoteMediator(
    private val database: PokemonDatabase,
    private val apiService: PokeApiService
) : RemoteMediator<Int, PokemonEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PokemonEntity>
    ): MediatorResult {
        val offset = when (loadType) {
            LoadType.REFRESH -> 0
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
            val limit = state.config.pageSize
            val response = apiService.getPokemonList(limit = limit, offset = offset)
            val endOfPaginationReached = response.results.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.remoteKeyDao().clearRemoteKeys()
                    database.pokemonDao().clearAll()
                }

                val prevKey = if (offset == 0) null else offset - limit
                val nextKey = if (endOfPaginationReached) null else offset + limit
                
                val entities = response.results.map { dto ->
                    PokemonEntity(
                        id = dto.id,
                        name = dto.name.replaceFirstChar { it.uppercase() },
                        imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${dto.id}.png"
                    )
                }
                
                val keys = entities.map {
                    RemoteKeyEntity(pokemonId = it.id, prevKey = prevKey, nextKey = nextKey)
                }

                database.remoteKeyDao().insertAll(keys)
                database.pokemonDao().insertAll(entities)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, PokemonEntity>): RemoteKeyEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { pokemon ->
            database.remoteKeyDao().getRemoteKeyByPokemonId(pokemon.id)
        }
    }
}