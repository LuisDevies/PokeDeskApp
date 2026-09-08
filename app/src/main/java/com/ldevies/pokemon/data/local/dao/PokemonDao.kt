package com.ldevies.pokemon.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ldevies.pokemon.data.local.entity.PokemonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {

    @Query("SELECT * FROM pokemon ORDER BY id ASC")
    fun getAllPokemon(): PagingSource<Int, PokemonEntity>

    @Query("SELECT * FROM pokemon WHERE name LIKE '%' || :query || '%' ORDER BY id ASC")
    fun searchPokemon(query: String): PagingSource<Int, PokemonEntity>

    @Query("SELECT * FROM pokemon WHERE id = :id")
    fun getPokemonById(id: Int): Flow<PokemonEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pokemonList: List<PokemonEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemon(pokemon: PokemonEntity)

    @Query("DELETE FROM pokemon")
    suspend fun clearAll()

}