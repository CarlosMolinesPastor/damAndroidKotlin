package com.karlinux.datosalumno3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.karlinux.datosalumno3.databinding.FragmentHistoricoBinding
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


class FragmentHistorico : Fragment() {

    private lateinit var binding: FragmentHistoricoBinding
    private lateinit var myAdapter: DatosAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHistoricoBinding.inflate(inflater)
        setUpRecyclerView()
        return binding.root
    }

    //Cuando se reanuda la actividad se vuelve a cargar el recycler view
    override fun onResume() {
        setUpRecyclerView()
        super.onResume()
    }

    //Funcion para leer el fichero y mostrarlo en el recycler view
    private fun setUpRecyclerView() {
        // Esta opción a TRUE significa que el RV tendrá
        // hijos del mismo tamaño, optimiza su creación.
        binding.myRVDatos.setHasFixedSize(true)
        // Se indica el contexto para RV en forma de lista.
        binding.myRVDatos.layoutManager = LinearLayoutManager(requireContext())
        // Se genera el adapter. Con la lista que devuelve al leer el fichero con la funcion
        // leerFichero y el contexto
        myAdapter = DatosAdapter(leerFichero(), activity as MainActivity)

        // Se asigna el adapter al RV.
        binding.myRVDatos.adapter = myAdapter
    }

    //Funcion para leer el fichero
    private fun leerFichero(): MutableList<Datos> {
        //Creamos una lista de datos, para despues cuando nos pongamos linea por linea en
        // el bucle del while vayamos añadiendo y posteriomente la devolvemos para pasarla
        // al adapter
        val listaDatos : MutableList<Datos> = arrayListOf()
        // Se comprueba si existe el fichero.
        if (requireContext().fileList().contains(getString(R.string.filename))) {
            try {
                val entrada = InputStreamReader(requireContext().openFileInput(getString(R.string.filename)))
                val br = BufferedReader(entrada)
                // Leemos la primera línea
                var linea = br.readLine()
                //Mientras que la linea no sea nula o vacia
                while (!linea.isNullOrEmpty()) {
                    // Obtenemos los datos separandolo por el ;
                    val datos: List<String> = linea.split(";")
                    // Creamos un objeto de la clase Datos
                    listaDatos.add(Datos(datos[0],datos[1],datos[2],datos[3],datos[4],datos[5],datos[6]))
                    // Leemos la siguiente línea del fichero
                    linea = br.readLine()
                }
                br.close()
                entrada.close()
            } catch (e: IOException) {
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(requireContext(),R.string.no_existe_fichero,Toast.LENGTH_LONG).show()
        }
        //Devolvemos la lista de datos
        return listaDatos
    }
}
