package com.ldevies.pokemon.data.remote.models

data class PokemonDetailResponse(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int
)