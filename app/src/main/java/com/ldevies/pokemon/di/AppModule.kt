package com.ldevies.pokemon.di

import android.content.Context
import androidx.room.Room
import com.ldevies.pokemon.data.local.PokemonDatabase
import com.ldevies.pokemon.data.local.dao.PokemonDao
import com.ldevies.pokemon.data.remote.api.PokeApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePokeApiService(): PokeApiService = Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(PokeApiService::class.java)

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): PokemonDatabase = Room.databaseBuilder(
        context,
        PokemonDatabase::class.java,
        "pokemon_db"
    ).build()

    @Provides
    fun providePokemonDao(db: PokemonDatabase): PokemonDao = db.pokemonDao()
}