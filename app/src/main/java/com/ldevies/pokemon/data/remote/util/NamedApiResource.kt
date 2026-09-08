package com.ldevies.pokemon.data.remote.util

data class NamedApiResource(val name: String, val url: String) {
    // Helper to extract ID from URL like "https://pokeapi.co/api/v2/pokemon/1/"
    val id: Int get() = url.trimEnd('/').substringAfterLast('/').toIntOrNull() ?: 0
}