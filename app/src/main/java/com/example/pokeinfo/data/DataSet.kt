package com.example.pokeinfo.data

import android.view.Display.Mode
import com.example.pokeinfo.R
import com.example.pokeinfo.model.Marca
import com.example.pokeinfo.model.Modelo

class DataSet {

    // conjunto de datos, accedido de forma directa
    // los metodos que aqui voy a crear no pertenecen al objeto
    // pertenecen a la clase
    // DataSet.metodo() -> static

    companion object{
        fun getAllMarcas(): ArrayList<Marca> {

            val listaMarcar = ArrayList<Marca>()

            listaMarcar.add(Marca("Mercedes","premium", R.drawable.missigno))
            listaMarcar.add(Marca("Audi","premium", R.drawable.missigno))
            listaMarcar.add(Marca("VW","alta", R.drawable.missigno))
            listaMarcar.add(Marca("Ford","normal", R.drawable.missigno))

            return listaMarcar

        }

        fun getAllModelos(marcaSeleccionada: String) : ArrayList<Modelo>{
            val listaModelos = ArrayList<Modelo>();
            listaModelos.add(Modelo("Mercedes","C220",200,50000.0,R.drawable.missigno))
            listaModelos.add(Modelo("Mercedes","GLC",400,80000.0,R.drawable.missigno))
            listaModelos.add(Modelo("Audi","A8",500,90000.0,R.drawable.missigno))
            listaModelos.add(Modelo("Audi","E-Tron",300,70000.0,R.drawable.missigno))
            listaModelos.add(Modelo("Ford","Mustang",450,60000.0,R.drawable.missigno))
            listaModelos.add(Modelo("Ford","Focus",200,30000.0,R.drawable.missigno))
            listaModelos.add(Modelo("VW","Arteon",200,50000.0,R.drawable.missigno))
            listaModelos.add(Modelo("VW","Taigo",200,60000.0,R.drawable.missigno))

            return listaModelos.filter {
                return@filter it.marca.equals(marcaSeleccionada)
            } as ArrayList<Modelo>;
        }
    }

}