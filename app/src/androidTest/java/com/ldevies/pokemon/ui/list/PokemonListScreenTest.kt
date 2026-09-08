package com.ldevies.pokemon.ui.list

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.ldevies.pokemon.data.local.entity.PokemonEntity
import com.ldevies.pokemon.data.repository.FakePokemonRepository
import org.junit.Rule
import org.junit.Test

class PokemonListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val fakeRepository = FakePokemonRepository()
    private val viewModel = PokemonListViewModel(fakeRepository)

    @Test
    fun pokemonList_displaysItems() {
        // Given
        val pokemon = PokemonEntity(1, "Bulbasaur", "url")
        fakeRepository.emitPokemon(listOf(pokemon))

        // When
        composeTestRule.setContent {
            PokemonListScreen(
                viewModel = viewModel,
                onPokemonClick = {}
            )
        }

        // Then
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithText("Bulbasaur").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithText("Bulbasaur").assertIsDisplayed()
    }

    @Test
    fun clickingPokemon_triggersCallback() {
        // Given
        var clickedId = -1
        val pokemon = PokemonEntity(1, "Bulbasaur", "url")
        fakeRepository.emitPokemon(listOf(pokemon))

        // When
        composeTestRule.setContent {
            PokemonListScreen(
                viewModel = viewModel,
                onPokemonClick = { clickedId = it }
            )
        }

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithText("Bulbasaur").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithText("Bulbasaur").performClick()

        // Then
        assert(clickedId == 1)
    }

    @Test
    fun typingInSearch_updatesSearchQuery() {
        // Given
        fakeRepository.emitPokemon(emptyList())

        // When
        composeTestRule.setContent {
            PokemonListScreen(
                viewModel = viewModel,
                onPokemonClick = {}
            )
        }

        val searchText = "Pikachu"
        composeTestRule.onNodeWithText("Search Pokémon...").performTextInput(searchText)

        // Then
        assert(viewModel.searchQuery.value == searchText)
    }
}