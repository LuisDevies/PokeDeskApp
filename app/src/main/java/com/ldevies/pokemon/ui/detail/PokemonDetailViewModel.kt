package com.ldevies.pokemon.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ldevies.pokemon.data.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val repository: PokemonRepository,
    // SavedStateHandle automatically contains navigation arguments passed via NavGraph
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Extract pokemonId type-safely from navigation parameters
    private val pokemonId: Int = checkNotNull(savedStateHandle["pokemonId"])

    val detailUiState: StateFlow<PokemonDetailUiState> = repository.getPokemonById(pokemonId)
        .map { entity ->
            if (entity != null) {
                PokemonDetailUiState.Success(pokemon = entity)
            } else {
                PokemonDetailUiState.Loading
            }
        }
        .catch { throwable ->
            emit(PokemonDetailUiState.Error(message = throwable.message ?: "Failed to load Pokémon details"))
        }
        .stateIn(
            scope = viewModelScope,
            // MEMORY LEAK PREVENTION: Stops database flow collection 5 seconds after screen is detached
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PokemonDetailUiState.Loading
        )

    init {
        // Trigger background refresh from network to fetch full stats (height/weight)
        refreshDetail()
    }

    private fun refreshDetail() {
        viewModelScope.launch {
            repository.refreshPokemonDetail(pokemonId)
        }
    }
}