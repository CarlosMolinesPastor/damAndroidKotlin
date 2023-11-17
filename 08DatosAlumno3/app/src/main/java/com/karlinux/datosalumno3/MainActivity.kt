// ######## MAIN ACTIVITY ##########
package com.karlinux.datosalumno3

//Carlos Molines Pastor (karlinux)
// 2º DAM Semipresencial


import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.karlinux.datosalumno3.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding;
    //Trabajamoscon binding previa declaracion en gradleapp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewPager2 = binding.viewPager2
        // Se crea el adapter.
        val adapter = ViewPager2Adapter(supportFragmentManager, lifecycle)
        // Se añaden los fragments y los títulos de pestañas.
        // Es lo unico que se ha tenido que modificar del codigo original
        adapter.addFragment(FragmentPrincipal(), "Datos")
        adapter.addFragment(FragmentHistorico(),"Historico")
        // Se asocia el adpater al viewPager2
        viewPager2.adapter = adapter
        // Carga de las pestañas en el TabLayout
        TabLayoutMediator(binding.tabLayout, viewPager2){tab, position ->
            tab.text = adapter.getPageTitle(position)}.attach()
    }


}


