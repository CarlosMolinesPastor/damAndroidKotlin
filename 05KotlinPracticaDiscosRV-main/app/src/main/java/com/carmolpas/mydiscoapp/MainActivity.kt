package com.carmolpas.mydiscoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.carmolpas.mydiscoapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var myAdapter: DiscoAdapter
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Estas dos líneas sustituyen a
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        // Esta opción a TRUE significa que el RV tendrá
        // hijos del mismo tamaño, optimiza su creación.
        binding.myRVDiscos.setHasFixedSize(true)
        // Se indica el contexto para RV en forma de lista.
        binding.myRVDiscos.layoutManager = LinearLayoutManager(this)
        // Se genera el adapter.
        myAdapter = DiscoAdapter(getDiscos(), this)

        // Se asigna el adapter al RV.
        binding.myRVDiscos.adapter = myAdapter
    }
    private fun getDiscos(): MutableList<MyDisco> {
        val discos: MutableList<MyDisco> = arrayListOf()
        discos.add(MyDisco("Abbey Road", "The Beatles", R.mipmap.abbey))
        discos.add(MyDisco("Born to Run", "Bruce Springsteen", R.mipmap.born))
        discos.add(MyDisco("Exile on main st.", "The Rolling Stones", R.mipmap.exile))
        discos.add(MyDisco("Ten", "Pearl Jam", R.mipmap.ten))
        discos.add(MyDisco("Nocturnal", "Amaral", R.mipmap.nocturnal))
        discos.add(MyDisco("Daiquiri Blues", "Quique González", R.mipmap.daiquiri))
        discos.add(MyDisco("White Album", "The Beatles", R.mipmap.white))
        discos.add(MyDisco("24 Nights", "Eric Clapton", R.mipmap.nights))
        discos.add(MyDisco("Shake your moneymaker", "The Black Crowes", R.mipmap.shake))
        return discos
    }
}