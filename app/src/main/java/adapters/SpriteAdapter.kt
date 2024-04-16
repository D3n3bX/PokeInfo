package adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pokeinfo.R
import com.example.pokeinfo.model.Pokemon

class SpriteAdapter(var context: Context, val imageUrls: List<String?>): RecyclerView.Adapter<SpriteAdapter.MyHolder>()  {

    // Creamos la clase MyHolder que implementa RecyclerView.ViewHolder(itemView)
    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // Creamos los atributos que tiene el RecyclerView
        var sprite: ImageView = itemView.findViewById(R.id.sprite) // Sprite del Pokemon

    }

    // Sobreescribimos la función onCreateViewHolder(parent: ViewGroup, viewType: Int)
    // la cual implementa MyHolder para crear la plantilla de cada fila
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {

        // Inflamos la vista del elemento de RecyclerView desde el archivo XML del layout
        val view: View = LayoutInflater.from(context)
            .inflate(R.layout.sprite_image, parent, false)

        // Devolvemos una instancia de MyHolder que contiene la vista creada
        return MyHolder(view)

    }

    // número de elementos - filas que se tienen que pintar
    override fun getItemCount(): Int {
        // Devolvemos el tamaño de la lista y con ello el número de elementos que hay
        return imageUrls.size
    }

    // Sobreescribimos la función onBindViewHolder(holder: MyHolder, position: Int) para asociar
    // los datos con la plantilla
    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val imageUrl = imageUrls[position]
        Glide.with(holder.sprite)
            .load(imageUrl)
            .placeholder(R.drawable.missigno)
            .into(holder.sprite)
    }

    // Método para añadir un nuevo Pokemon a la lista y notificar el cambio
    fun addPokemon() {

        // Notificamos el cambio en el RecyclerView
        notifyItemInserted(imageUrls.size - 1)
    }
}
