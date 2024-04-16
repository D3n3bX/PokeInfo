package adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pokeinfo.R
import com.example.pokeinfo.model.Pokemon
import com.example.pokeinfo.ui.InfoActivity

class PokemonAdapter (var context: Context): RecyclerView.Adapter<PokemonAdapter.MyHolder>()  {

    // Creamos un ArrayList de Pokemon
    var listaDatos: ArrayList<Pokemon> = ArrayList()

    // Creamos la clase MyHolder que implementa RecyclerView.ViewHolder(itemView)
    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // Creamos los atributos que tiene el RecyclerView
        var imagen: ImageView = itemView.findViewById(R.id.imagenFila) // Sprite del Pokemon
        var titulo: TextView = itemView.findViewById(R.id.tituloFila) // Nombre del Pokemon
        var subTitulo: TextView = itemView.findViewById(R.id.subtituloFila) // Tipo(s) del Pokemon
        var imagen2: ImageView = itemView.findViewById(R.id.imagen2Fila) // Tipo 1 del Pokemon
        var imagen3: ImageView = itemView.findViewById(R.id.imagen3Fila) // Tipo 2 del Pokemon
        var button: Button = itemView.findViewById(R.id.buttonInfo) // Botón para pasar de pantalla
    }

    // Sobreescribimos la función onCreateViewHolder(parent: ViewGroup, viewType: Int)
    // la cual implementa MyHolder para crear la plantilla de cada fila
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {

        // Inflamos la vista del elemento de RecyclerView desde el archivo XML del layout
        val view: View = LayoutInflater.from(context)
            .inflate(R.layout.item_recycler, parent, false)

        // Devolvemos una instancia de MyHolder que contiene la vista creada
        return MyHolder(view)
    }

    // número de elementos - filas que se tienen que pintar
    override fun getItemCount(): Int {
        // Devolvemos el tamaño de la lista y con ello el número de elementos que hay
        return listaDatos.size
    }

    // Sobreescribimos la función onBindViewHolder(holder: MyHolder, position: Int) para asociar
    // los datos con la plantilla
    override fun onBindViewHolder(holder: MyHolder, position: Int) {

        // Obtenemos el Pokemon en la posición dada
        val pokemon = listaDatos[position]

        // Asignamos el nombre del Pokemon al TextView correspondiente
        holder.titulo.text = pokemon.name

        // Creamos un StringBuilder para poder formatear los tipos -> la API lo devuelve como un array
        val formattedTypes = StringBuilder()

        // Recorremos los tipos de Pokemon
        for (type in pokemon.types) {
            formattedTypes.append(type.type.name).append(", ")
        }

        // Si formattedTypes no esta vacío
        if (formattedTypes.isNotEmpty()) {
            // Eliminamos la última coma y espacio si hay tipos presentes
            formattedTypes.delete(formattedTypes.length - 2, formattedTypes.length)
        }

        // Asignamos los tipos del Pokemon al TextView correspondiente
        holder.subTitulo.text = formattedTypes.toString()

        // Asignamos las imágenes de los tipos de Pokémon
        // El primer tipo siempre se va a asignar al primer ImageView
        if (pokemon.types.size > 0) {
            val type = pokemon.types[0].type.name

            // Obtenemos el recurso necesario del drawable
            val imageResourceId = context.resources.getIdentifier("$type", "drawable", context.packageName)

            // Asignamos el primer tipo del Pokemon al ImageView
            holder.imagen2.setImageResource(imageResourceId)
        }

        // El segundo tipo puede no estar, depende de si hay dos tipos o no
        if (pokemon.types.size > 1) {
            val type = pokemon.types[1].type.name

            // Obtenemos el recurso necesario del drawable
            val imageResourceId = context.resources.getIdentifier("$type", "drawable", context.packageName)

            // Asignamos el segudo tipo del Pokemon al ImageView
            holder.imagen3.setImageResource(imageResourceId)
        } else {
            // Si solo hay un tipo, dejamos el segundo ImageView sin imagen
            holder.imagen3.setImageDrawable(null)
        }

        // Obtenemos la URL de la imagen del Pokemon
        val imageUrl = pokemon.sprites.frontDefault

        // Asignamos la imagen del Pokemon al ImageView correspondiente utilizando Glide
        Glide.with(context) // Necesitamos un contexto
            .load(imageUrl) // Cargamos la imagen desde la URL
            .placeholder(R.drawable.missigno) // Imagen por defecto, si no carga la URL
            .into(holder.imagen) // Asignamos la imagen al ImageView

        // Agregar OnClickListener al botón
        holder.button.setOnClickListener {
            // Acción a realizar al pulsar el botón
            // Aquí debes iniciar la nueva actividad
            // Por ejemplo:

            // Pasar a una segunda pantalla -> Info
            // Creamos un contexto
            val context = holder.itemView.context

            // Indicamos el origen (this o applicationContext) y el destino (<nombre>::class.java)
            // Creamos un intent para poder recuperar los datos en la siguiente pantalla
            val intent = Intent(context, InfoActivity::class.java)

            // Pasamos el Pokemon
            intent.putExtra("pokemon", pokemon)



            // Iniciamos la pantalla Info
            context.startActivity(intent)

        }
    }

    // Método para añadir un nuevo Pokemon a la lista y notificar el cambio
    fun addPokemon(pokemon: Pokemon) {

        // Añadimos el Pokemon a la lista
        listaDatos.add(pokemon)

        // Ordenamos la lista por ID (número de Pokedex) -> al principio salen desordenados
        listaDatos.sortBy { it.id }

        // Notificamos el cambio en el RecyclerView
        notifyItemInserted(listaDatos.size - 1)
    }

    fun filtrarPorTipo(tipo: String) {
        // Aplicar el filtro y actualizar la lista de Pokémon mostrados en el RecyclerView
        val pokemonFiltrados = if (tipo == "Todos") {
            // Mostrar todos los Pokémon si se selecciona "Todos"
            listaDatos
        } else {
            // Filtrar Pokémon por el tipo seleccionado
            listaDatos.filter { pokemon ->
                pokemon.types.any { pokemonType ->
                    pokemonType.type.name.equals(tipo, ignoreCase = true)
                }
            }
        }
        // Actualizar la lista de Pokémon en el adaptador
        listaDatos.clear()
        listaDatos.addAll(pokemonFiltrados)
        notifyDataSetChanged()
    }



}
