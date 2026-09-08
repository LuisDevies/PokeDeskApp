package com.ldevies.pokemon.data.repository

import com.ldevies.pokemon.data.local.PokemonDatabase
import com.ldevies.pokemon.data.local.dao.PokemonDao
import com.ldevies.pokemon.data.local.entity.PokemonEntity
import com.ldevies.pokemon.data.remote.api.PokeApiService
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PokemonRepositoryImplTest {

    private lateinit var repository: PokemonRepositoryImpl
    private val apiService: PokeApiService = mockk()
    private val database: PokemonDatabase = mockk()
    private val dao: PokemonDao = mockk()

    @Before
    fun setUp() {
        every { database.pokemonDao() } returns dao
        repository = PokemonRepositoryImpl(apiService, database)
    }

    @Test
    fun `getPokemonList returns a flow of PagingData`() {
        // When
        val result = repository.getPokemonList()

        // Then
        assertNotNull(result)
    }
}