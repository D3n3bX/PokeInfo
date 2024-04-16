package com.example.pokemoninfo.model

import java.io.Serializable

// Implementamos la interfaz de Serializable
class User (var nombre: String, var correo: String, var pass: String): Serializable {
}