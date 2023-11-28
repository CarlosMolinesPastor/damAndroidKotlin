package com.karlinux.myagendabd

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.karlinux.myagendabd.databinding.ActivityMainBinding
import com.karlinux.myagendabd.databinding.DialogoBinding

const val UPDATE = "update"
const val DELETE = "delete"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var agendaDBHelper: MyAgendaDBOpenHelper
    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)
            // Se instancia el objeto MyDBOpenHelper.
            agendaDBHelper = MyAgendaDBOpenHelper(this)
            with(binding) {
                // Botón INSERTAR.
                btnInsertar.setOnClickListener {
                    if (etNombre.text.isNotBlank() && etTlf.text.isNotBlank()) {
                        // Se inserta en la tabla.
                        agendaDBHelper.addContacto(
                            etNombre.text.toString(),
                            etTlf.text.toString()
                        )
                        // Se limpian los EditText después de la inserción.
                        etNombre.text.clear()
                        etTlf.text.clear()
                    }
                    else {
                        myToast("Los campos no pueden estar vacíos.")
                    }
                }

                // Botón ACTUALIZAR.
                btnActualizar.setOnClickListener {
                    if (etNombre.text.isNotBlank() && etTlf.text.isNotBlank()) {
                        // Se lanza el dialogo para solicitar el id del registro,
                        // además, se indica el tipo de operación.
                        solicitaIdentificador(UPDATE)
                    } else {
                        myToast("Los campos no pueden estar vacíos.")
                    }
                }
                // Botón ELIMINAR.
                btnEliminar.setOnClickListener {
                    // Se lanza el dialogo para solicitar el id del registro,
                    // además, se indica el tipo de operación.
                    solicitaIdentificador(DELETE)
                }

                // Botón CONSULTAR.
                btnConsultar.setOnClickListener() {
                    //tvResult.text = ""
                    /*val db: SQLiteDatabase = agendaDBHelper.readableDatabase
                    val cursor: Cursor = db.rawQuery(
                        "SELECT * FROM ${MyAgendaDBOpenHelper.TABLA_CONTACTOS};",
                        null
                    )
                    // Se comprueba que al menos exista un registro.
                    if (cursor.moveToFirst()) {
                        do {
                            tvResult.append(cursor.getInt(0).toString() + " - ")
                            tvResult.append(cursor.getString(1).toString() + " ")
                            tvResult.append(cursor.getString(2).toString() + "\n")
                        } while (cursor.moveToNext())
                    } else {
                        myToast("No existen datos a mostrar.")
                    }
                    db.close()*/
                    // Se cierra el cursor.

                    tvResult.text = agendaDBHelper.mostrarContactos()
                }
            }
        }

    fun solicitaIdentificador(accion: String) {
        val builder = AlertDialog.Builder(this)
        builder.apply {
            var bindingDialogo: DialogoBinding
            bindingDialogo = DialogoBinding.inflate(layoutInflater)
            setView(bindingDialogo.root)
            setPositiveButton(android.R.string.ok) { dialog, _ ->
                //val valor = (dialog as AlertDialog).identificador.text
                val valor = bindingDialogo.identificador.text
                val identificador = valor.toString().toInt()
                // Se realiza la acción.
                when (accion) {
                    UPDATE -> {
                        val nombre = binding.etNombre.text.toString()
                        val tlf = binding.etTlf.text.toString()
                        agendaDBHelper.updateContacto(identificador, nombre, tlf)

                        // Se limpian los EditText después de la inserción.
                        binding.etNombre.text.clear()
                        binding.etTlf.text.clear()
                    }
                    DELETE -> {
                        myToast(
                            "Eliminado/s " +
                                    "${agendaDBHelper.delContacto(identificador)} " +
                                    "registro/s"
                        )
                    }
                }
            }
            setNegativeButton(android.R.string.cancel) { dialog, _ ->
                    dialog.dismiss()
                }
        }.show()
    }
    fun myToast(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
}

