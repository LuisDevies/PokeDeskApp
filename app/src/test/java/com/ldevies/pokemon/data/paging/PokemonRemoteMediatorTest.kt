package com.ldevies.pokemon.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.ldevies.pokemon.data.local.PokemonDatabase
import com.ldevies.pokemon.data.local.entity.PokemonEntity
import com.ldevies.pokemon.data.remote.api.PokeApiService
import com.ldevies.pokemon.data.remote.dto.PokemonListResponse
import com.ldevies.pokemon.data.remote.util.NamedApiResource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalPagingApi::class, ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class PokemonRemoteMediatorTest {

    private lateinit var database: PokemonDatabase
    private val apiService: PokeApiService = mockk()
    private lateinit var remoteMediator: PokemonRemoteMediator

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            PokemonDatabase::class.java
        ).allowMainThreadQueries().build()
        
        remoteMediator = PokemonRemoteMediator(database, apiService)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `refresh load returns Success when more data is present`() = runTest {
        // Given
        val mockResponse = PokemonListResponse(
            results = listOf(
                NamedApiResource("Pikachu", "https://pokeapi.co/api/v2/pokemon/25/")
            )
        )
        coEvery { apiService.getPokemonList(any(), any()) } returns mockResponse

        val pagingState = PagingState<Int, PokemonEntity>(
            pages = listOf(),
            anchorPosition = null,
            config = PagingConfig(pageSize = 20),
            leadingPlaceholderCount = 0
        )

        // When
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)

        // Then
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun `refresh load returns Success and endOfPaginationReached when no more data`() = runTest {
        // Given
        val mockResponse = PokemonListResponse(results = emptyList())
        coEvery { apiService.getPokemonList(any(), any()) } returns mockResponse

        val pagingState = PagingState<Int, PokemonEntity>(
            pages = listOf(),
            anchorPosition = null,
            config = PagingConfig(pageSize = 20),
            leadingPlaceholderCount = 0
        )

        // When
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)

        // Then
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun `refresh load returns Error when exception occurs`() = runTest {
        // Given
        coEvery { apiService.getPokemonList(any(), any()) } throws RuntimeException("Network Error")

        val pagingState = PagingState<Int, PokemonEntity>(
            pages = listOf(),
            anchorPosition = null,
            config = PagingConfig(pageSize = 20),
            leadingPlaceholderCount = 0
        )

        // When
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)

        // Then
        assertTrue(result is RemoteMediator.MediatorResult.Error)
    }
}