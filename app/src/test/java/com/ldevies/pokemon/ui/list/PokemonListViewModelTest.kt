package com.ldevies.pokemon.ui.list

import androidx.paging.PagingData
import app.cash.turbine.test
import com.ldevies.pokemon.data.local.entity.PokemonEntity
import com.ldevies.pokemon.data.repository.PokemonRepository
import com.ldevies.pokemon.util.MainDispatcherRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalCoroutinesApi::class)
class PokemonListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    private val repository: PokemonRepository = mockk()
    private lateinit var viewModel: PokemonListViewModel

    @Before
    fun setUp() {
        // Default mock for initial state
        every { repository.getPokemonList(any()) } returns flowOf(PagingData.empty())
    }

    @Test
    fun `search query updates and triggers debounced repository call`() = runTest(testDispatcher) {
        // Given
        every { repository.getPokemonList(null) } returns flowOf(PagingData.empty())
        every { repository.getPokemonList("Pika") } returns flowOf(PagingData.empty())

        viewModel = PokemonListViewModel(repository)

        viewModel.pokemonPagingData.test {
            // Initial call for ""
            advanceTimeBy(301.milliseconds)
            awaitItem() 

            // When: Type "Pika"
            viewModel.onSearchQueryChanged("Pika")

            // Then: verify no call yet (debouncing 300ms)
            verify(exactly = 0) { repository.getPokemonList("Pika") }

            // When: Advance time
            advanceTimeBy(301.milliseconds)
            
            runCurrent()
            
            awaitItem()
        }
        
        verify(exactly = 1) { repository.getPokemonList("Pika") }
    }

    @Test
    fun `rapid query changes are debounced`() = runTest(testDispatcher) {
        // Given
        every { repository.getPokemonList(any()) } returns flowOf(PagingData.empty())

        viewModel = PokemonListViewModel(repository)

        viewModel.pokemonPagingData.test {
            // Initial call for ""
            advanceTimeBy(301.milliseconds)
            awaitItem()

            // When: Typing "P", "Pi", "Pik" rapidly
            viewModel.onSearchQueryChanged("P")
            advanceTimeBy(100.milliseconds)
            viewModel.onSearchQueryChanged("Pi")
            advanceTimeBy(100.milliseconds)
            viewModel.onSearchQueryChanged("Pik")
            
            // Advance past the 300ms debounce for "Pik"
            advanceTimeBy(301.milliseconds)
            
            runCurrent()
            
            awaitItem()

            // Then: verify only "Pik" triggered a repository call
            verify(exactly = 1) { repository.getPokemonList("Pik") }
            verify(exactly = 0) { repository.getPokemonList("P") }
            verify(exactly = 0) { repository.getPokemonList("Pi") }
        }
    }
}