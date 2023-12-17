package com.karlinux.myjuegos

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.karlinux.myjuegos.databinding.ActivityVistaBinding



class Vista : AppCompatActivity() {
    private lateinit var binding: ActivityVistaBinding
    //Action Mode
    private var actionMode: ActionMode? = null
    companion object{
        const val COLUMNA_ID = "id"
        const val COLUMNA_NOMBRE = "nombre"
        const val COLUMNA_DESARROLLADOR = "desarrollador"
        const val COLUMNA_ANYO = "anyo"
        const val COLUMNA_IMAGEN = "imagen"
        var imagenstring = ""
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVistaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener la actividad que contiene esta actividad
        val activity = this as AppCompatActivity

        // Cambiar el color de la barra de herramientas
        activity.supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#0a6ef5")))

        // Recuperar los datos del intent, se podria hacer con una funcion en mydataDBOpenHelper
        // creando una data class de juego para almacenar los datos pasando solo el id de la BD
        // y recuperar el resto de datos de la BD, pero como son pocos datos los he realizado con intent
        val id = intent.getStringExtra(MyRecyclerViewAdapter.COLUMNA_ID)
        val nombre = intent.getStringExtra(MyRecyclerViewAdapter.COLUMNA_NOMBRE)
        val desarrollador = intent.getStringExtra(MyRecyclerViewAdapter.COLUMNA_DESARROLLADOR)
        val anyo = intent.getStringExtra(MyRecyclerViewAdapter.COLUMNA_ANYO)
        val imagen = intent.getStringExtra(MyRecyclerViewAdapter.COLUMNA_IMAGEN)
        // Convertir la imagen a String
        imagenstring = imagen.toString()

        // Asignar los datos a los elementos de la UI
        binding.txtId.text = id.toString()
        binding.txtNombreV.text = nombre
        binding.txtDesarrolladorV.text = desarrollador
        binding.txtAnyoV.text = anyo
        // Cargar la imagen con Glide
        Glide.with(this).load(imagen).into(binding.imageV)

        // Action Mode desde el layout
        binding.root.setOnLongClickListener{
            when (actionMode) {
                null -> {
                    actionMode = it.startActionMode(actionModeCallback)
                    it.isSelected = true
                    true
                }
                else -> false
            }
        }

    }
    // Action Mode desde el código
    private val actionModeCallback = object : ActionMode.Callback {
        // Inflar el menú contextual
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            val inflater = menuInflater
            inflater.inflate(R.menu.action_mode, menu)
            return true
        }
        override fun onPrepareActionMode(mode: ActionMode, menu: Menu):
                Boolean {
            return false
        }
        // Definir las acciones del menú contextual
        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            return when (item.itemId) {
                // Editar
                R.id.option01 -> {
                    enviarIntent()
                    return true
                }
                // Borrar
                R.id.option02 -> {
                    eliminarJuego()
                    return true
                }
                else -> false
            }
        }
        override fun onDestroyActionMode(p0: ActionMode?) {
            actionMode = null
        }
    }
    // Funciones para el menú contextual
    // Enviar los datos a EditarJuego
    fun enviarIntent(){
        val intent = Intent(this, EditarJuego::class.java)
        intent.putExtra(COLUMNA_ID, binding.txtId.text.toString().toInt())
        intent.putExtra(COLUMNA_NOMBRE, binding.txtNombreV.text.toString())
        intent.putExtra(COLUMNA_DESARROLLADOR, binding.txtDesarrolladorV.text.toString())
        intent.putExtra(COLUMNA_ANYO, binding.txtAnyoV.text.toString())
        intent.putExtra(COLUMNA_IMAGEN, imagenstring)
        startActivity(intent)
    }
    // Eliminar el juego de la BD
    fun eliminarJuego(){
        val builder = AlertDialog.Builder(this)
        builder.setMessage("¿Está seguro que desea eliminar el juego de la Base de Datos?")
            .setCancelable(false)
            .setPositiveButton("Sí") { dialog, id ->
                val datosDBHelper: MyDataDBOpenHelper = MyDataDBOpenHelper(this)
                datosDBHelper.delJuego(binding.txtId.text.toString().toInt())
                Toast.makeText(this, "Juego eliminado", Toast.LENGTH_SHORT).show()
                //Creamos un intent para volver al MainActivity e indicarle que queremos ver la lista
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                //Señal para pasarsela al MainActivity para indicarle que venimos de aqui
                // y asi que muestre el fragmento ListaJuegoFragment y no el principal
                intent.putExtra("verLista", true)
                startActivity(intent)
            }
                // Si se pulsa no se hace nada
            .setNegativeButton("No") { dialog, id ->
                dialog.cancel()
            }
        val alert = builder.create()
        alert.show()
    }
}