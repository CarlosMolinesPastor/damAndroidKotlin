package com.carmolpas.recicleview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.carmolpas.recicleview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private var listapeliculas : MutableList<Peliculas> = mutableListOf()
    private lateinit var recycler : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        establecerAdaptador()
        anadirPeliculas()
    }


    private fun anadirPeliculas() {
        listapeliculas.add(Peliculas("El senor de los anillos", "Peter Jackson"))
        listapeliculas.add(Peliculas("El Padrino", "Francis Ford Coppola"))
        listapeliculas.add(Peliculas("El Padrino II", "Francis Ford Coppola"))
        listapeliculas.add(Peliculas("Superman", "Richard Donner"))
        listapeliculas.add(Peliculas("Superman II", "Richard Donner"))
        listapeliculas.add(Peliculas("Batman", "Tim Burton"))
        listapeliculas.add(Peliculas("El caballero oscuro", "Christopher Nolan"))
        listapeliculas.add(Peliculas("El caballero oscuro: La leyenda renace", "Christopher Nolan"))
        listapeliculas.add(Peliculas("Drive", "Nicolas Winding Refn"))
        listapeliculas.add(Peliculas("007", "Sam Mendes"))
        listapeliculas.add(Peliculas("007: Spectre", "Sam Mendes"))
        listapeliculas.add(Peliculas("El silencio de los corderos", "Jonathan Demme"))
        listapeliculas.add(Peliculas("El club de la lucha", "David Fincher"))

    }
    private fun establecerAdaptador() {
        recycler = binding.idRVlista
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = Adapter(this, listapeliculas)
    }
}