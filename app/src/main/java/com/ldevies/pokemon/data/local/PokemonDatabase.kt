package com.ldevies.pokemon.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ldevies.pokemon.data.local.dao.PokemonDao
import com.ldevies.pokemon.data.local.dao.RemoteKeyDao
import com.ldevies.pokemon.data.local.entity.PokemonEntity
import com.ldevies.pokemon.data.local.entity.RemoteKeyEntity

@Database(entities = [PokemonEntity::class, RemoteKeyEntity::class], version = 1, exportSchema = false)
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
    abstract fun remoteKeyDao(): RemoteKeyDao
}