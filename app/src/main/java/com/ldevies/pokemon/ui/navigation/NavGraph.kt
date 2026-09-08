package com.ldevies.pokemon.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.ldevies.pokemon.ui.detail.PokemonDetailScreen
import com.ldevies.pokemon.ui.list.PokemonListScreen

@Composable
fun PokedexNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = PokemonListRoute,
        modifier = modifier
    ) {
        composable<PokemonListRoute> {
            PokemonListScreen(
                onPokemonClick = { pokemonId ->
                    navController.navigate(PokemonDetailRoute(pokemonId = pokemonId))
                }
            )
        }

        composable<PokemonDetailRoute> { backStackEntry ->
            val route: PokemonDetailRoute = backStackEntry.toRoute()
            PokemonDetailScreen(
                pokemonId = route.pokemonId,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}