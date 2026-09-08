package com.ldevies.pokemon.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object PokemonListRoute

@Serializable
data class PokemonDetailRoute(val pokemonId: Int)