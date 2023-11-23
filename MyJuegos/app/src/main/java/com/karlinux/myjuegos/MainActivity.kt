package com.karlinux.myjuegos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.tabs.TabLayoutMediator
import com.karlinux.myjuegos.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    // ## PRIMERO BINDING
    private lateinit var binding: ActivityMainBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Se obtiene el viewPager2
        val viewPager2 = binding.viewPager2
        // Se crea el adapter.
        val adapter = ViewPager2Adapter(supportFragmentManager, lifecycle)
          // Se añaden los fragments al adapter
        adapter.addFragment(JuegoNuevoFragment(), "Añadir Juego")
        adapter.addFragment(ListaJuegoFragment(),"Lista Juegos")
        // Se asigna el adapter al viewPager2
        viewPager2.adapter = adapter
        // Se asigna el viewPager2 al TabLayout
        TabLayoutMediator(binding.tabLayout, viewPager2){tab, position ->
            tab.text = adapter.getPageTitle(position)}.attach()
        // Se comprueba si se ha iniciado la actividad desde EditarJuego
        // para cambiar al fragmento ListaJuegoFragment y asi ver los cambios en el RecyclerView
        // Se crea variable extras para obtener los datos del intent
        val extras = intent.extras
        // Se comprueba si el intent contiene el valor de "fromEditarJuego" desde la señal que hemos
        // emitido en EditarJuego.kt
        if (extras != null && extras.containsKey("verLista")) {
            // Cambiar al fragmento ListaJuegoFragment
            viewPager2.setCurrentItem(1, false) // 1 representa la posición del fragmento ListaJuegoFragment
            // Eliminar el valor de "fromEditarJuego" del intent
            intent.removeExtra("verLista")
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuanyadir -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                return true
            }
            // Maneja otras acciones del menú si es necesario
        }
        return super.onOptionsItemSelected(item)
    }
}