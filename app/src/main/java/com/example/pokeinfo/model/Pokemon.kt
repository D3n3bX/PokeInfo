package com.example.pokeinfo.model

import java.io.Serializable

class Pokemon (
    var id: Int,
    var name: String,
    var height: Int,
    var weight: Int,
    var baseExperience: Int,
    var types: List<PokemonType>,
    var abilities: List<PokemonAbility>,
    var sprites: PokemonSprites,
    var trainer: String

) : Serializable {

    data class PokemonType(
        val slot: Int,
        val type: Type
    ) : Serializable {
        data class Type(
            val name: String,
            val url: String
        ) : Serializable
    }

    data class PokemonAbility(
        val ability: Ability,
        val isHidden: Boolean,
        val slot: Int
    ) : Serializable {
        data class Ability(
            val name: String,
            val url: String
        ) : Serializable
    }

    data class PokemonSprites(
        val backDefault: String?,
        val backShiny: String?,
        val frontDefault: String?,
        val frontShiny: String?,
    ) : Serializable
}
