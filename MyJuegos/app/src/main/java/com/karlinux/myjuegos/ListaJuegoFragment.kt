package com.karlinux.myjuegos

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.ActionMode
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.karlinux.myjuegos.databinding.FragmentListaJuegoBinding


class ListaJuegoFragment : Fragment()  {
    // ## PRIMERO BINDING
    private lateinit var binding: FragmentListaJuegoBinding
    //private lateinit var myAdapter: DatosAdapter
    private lateinit var datosDBHelper: MyDataDBOpenHelper
    // Se declara la variable para la base de datos.
    private lateinit var db: SQLiteDatabase
    //Action Mode
    private var actionMode: ActionMode? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Obtener la actividad que contiene este fragmento
        val activity = requireActivity() as AppCompatActivity
        // Cambiar el color de la barra de herramientas
        activity.supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#0acef5")))

        // Inflamos el layout para este fragmento.
        binding = FragmentListaJuegoBinding.inflate(inflater)
        datosDBHelper = MyDataDBOpenHelper(requireContext())

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
            "SELECT * FROM ${MyDataDBOpenHelper.TABLA_JUEGOS};", null
        )
        // Se crea el adaptador con el resultado del cursor.
        val myRecyclerViewAdapter = MyRecyclerViewAdapter(requireContext(), cursor)
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


}