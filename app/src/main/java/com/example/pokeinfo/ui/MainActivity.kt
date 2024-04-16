package com.example.pokeinfo.ui

import adapters.PokemonAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.pokeinfo.R
import com.example.pokeinfo.databinding.ActivityMainBinding
import com.example.pokeinfo.model.Marca
import com.example.pokeinfo.model.Pokemon


class MainActivity : AppCompatActivity() {

    // Creamos un binding asociado a ActivityMainBinding
    private lateinit var binding: ActivityMainBinding

    // Creamos una variable nombre
    private lateinit var nombre: String

    // Creamos un adaptador de Pokemon
    private lateinit var adaptadorPokemon: PokemonAdapter

    // Sobreescribimos la función onCreate(savedInstance: Bundle?) para iniciar la pantalla
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializamos el binding y lo "inflamos"
        binding = ActivityMainBinding.inflate(layoutInflater)

        // Establecemos la vista inflada como el contenido de la pantalla
        setContentView(binding.root)

        // Recuperamos el nombre de la pantalla anterior -> LoginActivity
        nombre = intent.getStringExtra("nombre")!! // Podemos asegurar que no va a ser null

        binding.textoSaludo.setText(nombre)

        // Orden de ejecución de las funciones
        instancias()
        personalizarRecycler()
        realizarPeticionJSON()

    }

    private fun realizarPeticionJSON() {

        // Creamos la url a donde se hará la petición
        // Esta URL nos devuelve el nombre del Pokemon junto con una URL para más información de él
        val url = "https://pokeapi.co/api/v2/pokemon"

        // Creamos la petición
        val peticion: JsonObjectRequest = JsonObjectRequest(url, { response ->

            // Obtenemos el JSONArray "results" de la respuesta
            val resultsArray = response.getJSONArray("results")

            // Iteramos sobre cada elemento del JSONArray
            for (i in 0 until resultsArray.length()) {

                // Obtenemos el objeto JSON de cada Pokémon
                val pokemonObject = resultsArray.getJSONObject(i)

                // Obtenemos el nombre y la URL del Pokémon
                val name = pokemonObject.getString("name")
                val pokemonUrl = pokemonObject.getString("url")

                // Llamamos a la función obtenerInfoPokemon
                obtenerInfoPokemon(name, pokemonUrl)
            }
        },
            { error ->
                // Manejamos errores de la petición
                Log.e("RequestError", "Error en la petición: $error")
            })

        // Agregamos la petición a la cola de solicitudes
        Volley.newRequestQueue(applicationContext).add(peticion)
    }

    private fun obtenerInfoPokemon(name: String, url: String) {

        // Creamos la petición
        val pokemonRequest = JsonObjectRequest(url, { response ->

                // Obtenmos id del Pokemon
                val pokemonId = response.getInt("id")

                // Obtenemos altura del Pokemon
                val height = response.getInt("height")

                // Obtenemos peso del Pokemon
                val weight = response.getInt("weight")

                // Obtenemos experiencia base del Pokemon
                val baseExperience = response.getInt("base_experience")

                // Obtenemos tipos del Pokémon
                // Obtenemos un JSONArray llamado "types" del objeto JSON response
                val typesArray = response.getJSONArray("types")

                // Inicializamos una lista mutable para almacenar los tipos de Pokémon
                val typesList = mutableListOf<Pokemon.PokemonType>()

                // Iteramos sobre cada elemento del JSONArray typesArray
                for (i in 0 until typesArray.length()) {

                    // Obtenemos el objeto JSON de cada tipo de Pokémon en la posición 'i'
                    val typeObject = typesArray.getJSONObject(i)

                    // Obtenemos el valor del atributo "slot" del tipo de Pokémon
                    val slot = typeObject.getInt("slot")

                    // Obtenemos el nombre y la URL del tipo de Pokémon
                    val type = Pokemon.PokemonType.Type(
                        typeObject.getJSONObject("type").getString("name"),
                        typeObject.getJSONObject("type").getString("url")
                    )

                    // Añadimos a la lista el tipo
                    typesList.add(Pokemon.PokemonType(slot, type))
                }

                // Obtenemos habilidades del Pokémon
                // Obtenemos un JSONArray llamado "abilities" del objeto JSON response
                val abilitiesArray = response.getJSONArray("abilities")

                // Inicializamos una lista mutable para almacenar los tipos de Pokémon
                val abilitiesList = mutableListOf<Pokemon.PokemonAbility>()

                // Iteramos sobre cada elemento del JSONArray typesArray
                for (i in 0 until abilitiesArray.length()) {

                    // Obtenemos el objeto JSON de cada habilidad de Pokémon en la posición 'i'
                    val abilityObject = abilitiesArray.getJSONObject(i)

                    // Obtenemos un booleano para saber si es habilidad oculta o no
                    val isHidden = abilityObject.getBoolean("is_hidden")

                    // Obtenemos el valor del atributo "slot" de la habilidad
                    val slot = abilityObject.getInt("slot")

                    // Obtenemos el nombre y la URL de la habilidad del Pokémon
                    val ability = Pokemon.PokemonAbility.Ability(
                        abilityObject.getJSONObject("ability").getString("name"),
                        abilityObject.getJSONObject("ability").getString("url")
                    )

                    // Añadimos a la lista la habilidad
                    abilitiesList.add(Pokemon.PokemonAbility(ability, isHidden, slot))
                }

                // Obtenemos sprites del Pokémon
                // Obtenemos un JSONArray llamado "sprites" del objeto JSON response
                val spritesObject = response.getJSONObject("sprites")

                // Obtenemos los diferentes sprites del Pokemon
                val sprites = Pokemon.PokemonSprites(
                    spritesObject.getString("back_default"),
                    spritesObject.getString("back_shiny"),
                    spritesObject.getString("front_default"),
                    spritesObject.getString("front_shiny")
                )

                // Creamos un objeto Pokemon
                val pokemon = Pokemon(pokemonId, name, height, weight, baseExperience, typesList, abilitiesList, sprites, nombre)

                // Añadimos al Pokemon
                adaptadorPokemon.addPokemon(pokemon)

            },
            { error ->
                // Manejamos errores de la petición
                Log.e("RequestError", "Error en la petición: $error")
            })

        // Agregamos la petición a la cola de solicitudes
        Volley.newRequestQueue(applicationContext).add(pokemonRequest)
    }

    fun instancias(){
        // Creamos una instancia del adaptador de Pokemon
        adaptadorPokemon = PokemonAdapter(this)

        // Crear ArrayAdapter para el Spinner
        val tiposPokemon = resources.getStringArray(R.array.tipos_pokemon)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, tiposPokemon)

        // Especificar el diseño del dropdown del Spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Asignar el adapter al Spinner
        binding.spinnerFilter.adapter = adapter

        // Agregar un listener al Spinner para manejar eventos de selección
        binding.spinnerFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Manejar la selección del tipo de Pokémon
                val tipoSeleccionado = tiposPokemon[position]
                filtrarPorTipo(tipoSeleccionado)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Manejar el caso cuando no se selecciona ningún tipo
            }
        }

    }

    fun personalizarRecycler(){

        // Asignamos el adaptador de Pokemon al RecyclerView
        binding.recyclerModelos.adapter = adaptadorPokemon

        // Configuramos el LinearLayoutManager para el RecyclerView con orientación vertical
        // y con el parámetro 'reverseLayout' en false (para ordenar los elementos desde arriba hacia abajo)
        binding.recyclerModelos.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    fun filtrarPorTipo(tipo: String) {
        // Filtrar los Pokémon en base al tipo seleccionado y actualizar el adaptador del RecyclerView
        adaptadorPokemon.filtrarPorTipo(tipo)
    }

}