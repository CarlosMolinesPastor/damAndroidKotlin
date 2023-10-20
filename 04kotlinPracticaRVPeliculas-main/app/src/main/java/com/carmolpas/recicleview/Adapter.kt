package com.carmolpas.recicleview

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class Adapter (var context :Context, var listaPeliculas :MutableList<Peliculas>)
    :RecyclerView.Adapter<Adapter.PeliculasHolder>(){

    inner class PeliculasHolder (view : View) :RecyclerView.ViewHolder(view){
        var titulo = view.findViewById(R.id.labelTitulo) as TextView
        var director = view.findViewById(R.id.labelDirector) as TextView

        fun bind (pelicula: Peliculas, context: Context){
            Log.d("bind",pelicula.titulo.toString())

            titulo.text = pelicula.titulo
            director.text = pelicula.director
            //Codigo si se hace click en el item
            itemView.setOnClickListener{
                Toast.makeText(context, pelicula.titulo, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeliculasHolder {
        var itemView = LayoutInflater.from(context).inflate(R.layout.carta, parent, false)
        return PeliculasHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listaPeliculas.size
    }

    override fun onBindViewHolder(holder: PeliculasHolder, position: Int) {
        //Lol que se ve en cada momento es decir la pelicula en cada momento
        var pelicula = listaPeliculas[position]
        //holder.titulo.text = pelicula.titulo
        //holder.director.text = pelicula.director
        holder.bind(pelicula, context)
    }

}