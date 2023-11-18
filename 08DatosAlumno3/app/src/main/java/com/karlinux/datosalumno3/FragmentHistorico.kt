// ### FRAGMENTO PARA MOSTRAR LOS DATOS DEL HISTORICO ###
package com.karlinux.datosalumno3

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.karlinux.datosalumno3.databinding.FragmentHistoricoBinding


class FragmentHistorico : Fragment() {

    private lateinit var binding: FragmentHistoricoBinding
    //private lateinit var myAdapter: DatosAdapter
    private lateinit var datosDBHelper: MyDatosDBOpenHelper
    // Se declara la variable para la base de datos.
    private lateinit var db: SQLiteDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflamos el layout para este fragmento.
        binding = FragmentHistoricoBinding.inflate(inflater)
        // Inicializar datosDBHelper
        datosDBHelper = MyDatosDBOpenHelper(requireContext())


        // Inicializar el RecyclerView.
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

        //Se crea el cursor oportuno
        db = datosDBHelper.readableDatabase
        val cursor: Cursor = db.rawQuery(
            "SELECT * FROM ${MyDatosDBOpenHelper.TABLA_ALUMNOS};", null
        )
        // Se crea el adaptador con el resultado del cursor.
        val myRecyclerViewAdapter = DatosAdapter(requireContext(), cursor)
        // Esta opción a TRUE significa que el RV tendrá
        // hijos del mismo tamaño, optimiza su creación.
        binding.myRVDatos.setHasFixedSize(true)
        // Se indica el contexto para RV en forma de lista.
        binding.myRVDatos.layoutManager = LinearLayoutManager(requireContext())
        // Se asigna el adapter al RV.
        binding.myRVDatos.adapter = myRecyclerViewAdapter
    }

    // Cerramos la conexión al terminar la activity.
    override fun onDestroy() {
        super.onDestroy()
        // Cerramos la conexión al terminar la activity.
        Log.d("onDestroy", "Cerramos la conexión")
        db.close()
    }
    // ########### FUNCION LEER FICHERO ###########
    /*Funcion para leer el fichero
    private fun leerFichero(): MutableList<Datos> {
        // Creamos una lista de datos, para despues cuando nos pongamos linea por linea en
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
    }*/
}
