package com.karlinux.myjuegos

import android.content.Intent
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

        // Recuperar los datos del intent
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
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            val inflater = menuInflater
            inflater.inflate(R.menu.action_mode, menu)
            return true
        }
        override fun onPrepareActionMode(mode: ActionMode, menu: Menu):
                Boolean {
            return false
        }
        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            return when (item.itemId) {
                R.id.option01 -> {
                    enviarIntent()
                    //Toast.makeText(applicationContext, R.string.editar, Toast.LENGTH_LONG).show()
                    return true
                }
                R.id.option02 -> {
                    eliminarJuego()
                    //Toast.makeText(applicationContext, R.string.borrar, Toast.LENGTH_LONG).show()
                    return true
                }
                else -> false
            }
        }
        override fun onDestroyActionMode(p0: ActionMode?) {
            actionMode = null
        }
    }
    fun enviarIntent(){
        val intent = Intent(this, EditarJuego::class.java)
        intent.putExtra(COLUMNA_ID, binding.txtId.text.toString().toInt())
        intent.putExtra(COLUMNA_NOMBRE, binding.txtNombreV.text.toString())
        intent.putExtra(COLUMNA_DESARROLLADOR, binding.txtDesarrolladorV.text.toString())
        intent.putExtra(COLUMNA_ANYO, binding.txtAnyoV.text.toString())
        intent.putExtra(COLUMNA_IMAGEN, imagenstring)
        startActivity(intent)
    }
    fun eliminarJuego(){
        val builder = AlertDialog.Builder(this)
        builder.setMessage("¿Está seguro que desea eliminar el juego de la Base de Datos?")
            .setCancelable(false)
            .setPositiveButton("Sí") { dialog, id ->
                val datosDBHelper: MyDataDBOpenHelper = MyDataDBOpenHelper(this)
                datosDBHelper.delJuego(binding.txtId.text.toString().toInt())
                Toast.makeText(this, "Juego eliminado", Toast.LENGTH_SHORT).show()
                //Creamos un intent para volver al MainActivity
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                //Señal para pasarsela al MainActivity para indicarle que venimos de aqui
                // y asi que muestre el fragmento ListaJuegoFragment y no el principal
                intent.putExtra("verLista", true)
                startActivity(intent)
            }
            .setNegativeButton("No") { dialog, id ->
                dialog.cancel()
            }
        val alert = builder.create()
        alert.show()
    }
}