package com.ldevies.pokemon.ui.detail

import com.ldevies.pokemon.data.local.entity.PokemonEntity

sealed interface PokemonDetailUiState {
    data object Loading : PokemonDetailUiState

    data class Success(
        val pokemon: PokemonEntity
    ) : PokemonDetailUiState

    data class Error(
        val message: String
    ) : PokemonDetailUiState
}