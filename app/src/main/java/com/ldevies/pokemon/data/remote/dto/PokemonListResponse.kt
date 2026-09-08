package com.ldevies.pokemon.data.remote.dto

import com.ldevies.pokemon.data.remote.util.NamedApiResource

data class PokemonListResponse(val results: List<NamedApiResource>)