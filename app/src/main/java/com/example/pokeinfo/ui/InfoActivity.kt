package com.example.pokeinfo.ui

import adapters.SpriteAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokeinfo.R
import com.example.pokeinfo.databinding.ActivityInfoBinding
import com.example.pokeinfo.model.Pokemon

class InfoActivity : AppCompatActivity() {

    // Creamos un binding asociado a ActivityLoginBinding
    private lateinit var binding: ActivityInfoBinding

    // Creamos un Pokemon
    private lateinit var pokemon: Pokemon

    // Creamos un adaptador de Pokemon
    private lateinit var adaptadorSprite: SpriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializamos el binding y lo "inflamos"
        binding = ActivityInfoBinding.inflate(layoutInflater)

        // Establecemos la vista inflada como el contenido de la pantalla
        setContentView(binding.root)

        // Recuperamos lo que nos ha pasado la pantalla de SignupActivity
        pokemon = intent.getSerializableExtra("pokemon") as Pokemon

        // Asignamos a la pantalla atributos básicos del Pokemon
        binding.textoNombre.text = pokemon.name
        binding.textoHeight.text = "${pokemon.height} dm"
        binding.textoWeight.text = "${pokemon.weight} hg"

        // Asiganamos a la pantalla el nombre del entrenador
        binding.textoTrainer.text = "Capturado por: ${pokemon.trainer}"

        // Obtenemos la habilidad del Pokemon
        val ability = pokemon.abilities.component1().ability.name

        // Asiganamos a la pantalla la habilidad del Pokemon
        binding.textoAbility.text = "Ability: $ability"

        // Cogemos el primer type del Pokemon
        val type1 = pokemon.types.component1().type.name

        // Asignamos a la pantalla el primer type del Pokemon
        binding.textoType1.text = type1

        // Obtenemos el recurso necesario del drawable
        val imageResourceId1 = this.resources.getIdentifier(type1, "drawable", this.packageName)

        // Asignamos el primer type del Pokemon al ImageView
        binding.type1.setImageResource(imageResourceId1)

        // Cogemos el segundo type del Pokemon si existe
        if (pokemon.types.size > 1) {
            val type2 = pokemon.types.component2().type.name

            // Obtenemos el recurso necesario del drawable
            val imageResourceId2 = this.resources.getIdentifier(type2, "drawable", this.packageName)

            // Asignamos el segundo type del Pokemon al ImageView
            binding.type2.setImageResource(imageResourceId2)

            // Asignamos a la pantalla el segundo type del Pokemon
            binding.textoType2.text = type2
        } else {
            // Si el Pokemon solo tiene un tipo, ocultamos el segundo tipo y su texto
            binding.textoType2.visibility = View.GONE
            binding.type2.visibility = View.GONE
        }

        // Configuramos el RecyclerView para mostrar las imágenes del Pokemon
        setupRecyclerView()
    }

    private fun setupRecyclerView() {

        val recyclerViewImages: RecyclerView = findViewById(R.id.sprite)

        // Obtenemos todos los sprites
        val imageUrl1 = pokemon.sprites.frontDefault
        val imageUrl2 = pokemon.sprites.backDefault
        val imageUrl3 = pokemon.sprites.frontShiny
        val imageUrl4 = pokemon.sprites.backShiny

        // Cramos una lista con las URLs
        val imageUrls = listOf(imageUrl1, imageUrl2, imageUrl3, imageUrl4)

        adaptadorSprite = SpriteAdapter(this, imageUrls)

        recyclerViewImages.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewImages.adapter = adaptadorSprite
    }
}
