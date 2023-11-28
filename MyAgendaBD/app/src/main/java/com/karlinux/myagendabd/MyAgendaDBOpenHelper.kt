package com.karlinux.myagendabd

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class MyAgendaDBOpenHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    val TAG = "SQLite"
    companion object {
        val DATABASE_VERSION = 1
        val DATABASE_NAME = "agenda.db"
        val TABLA_CONTACTOS = "contactos"
    }

    val COLUMNA_ID = "_id"
    val COLUMNA_NOMBRE = "nombre"
    val COLUMNA_TELEFONO = "tlf"

    /**
     * Este método es llamado cuando se crea la base por primera vez. Debe
     * producirse la creación de todas las tablas que formen la base de datos.
     */
    override fun onCreate(db: SQLiteDatabase?) {
        try {
            val crearTablaContactos = "CREATE TABLE $TABLA_CONTACTOS " +
                    "($COLUMNA_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMNA_NOMBRE TEXT, " +
                    "$COLUMNA_TELEFONO TEXT)"
            db!!.execSQL(crearTablaContactos)
        } catch (e: SQLiteException) {
            Log.e("$TAG (onCreate)", e.message.toString())
        }
    }

    /**
     * Este método se invocará cuando la base de datos necesite ser actualizada.
     * Se utiliza para hacer DROPs, añadir tablas o cualquier acción que
     * actualice el esquema de la BD.
     */
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        try {
            val dropTablaContactos = "DROP TABLE IF EXISTS $TABLA_CONTACTOS"
            db!!.execSQL(dropTablaContactos)
            onCreate(db)
        } catch (e: SQLiteException) {
            Log.e("$TAG (onUpgrade)", e.message.toString())
        }
    }

    /**
     * Método opcional. Se llamará a este método después de abrir la base de
     * datos, antes de ello, comprobará si está en modo lectura. Se llama justo
     * después de establecer la conexión y crear el esquema.
     */
    override fun onOpen(db: SQLiteDatabase?) {
        super.onOpen(db)
        Log.d("$TAG (onOpen)", "¡¡Base de datos abierta!!")
    }

    // CRUD
    fun addContacto(name: String, tlf: String) {
        // Se crea un ArrayMap<>() haciendo uso de ContentValues().
        val data = ContentValues()
        data.put(COLUMNA_NOMBRE, name)
        data.put(COLUMNA_TELEFONO, tlf)

        // Se abre la BD en modo escritura.
        val db = this.writableDatabase
        db.insert(TABLA_CONTACTOS, null, data)
        db.close()
    }

    fun delContacto(identifier: Int): Int {
        val args = arrayOf(identifier.toString())

        // Se abre la BD en modo escritura.
        val db = this.writableDatabase
        // Se puede elegir un sistema u otro.
        val result = db.delete(TABLA_CONTACTOS, "$COLUMNA_ID = ?", args)
        // db.execSQL("DELETE FROM $TABLA_CONTACTOS WHERE $COLUMNA_ID = ?", args)
        db.close()
        return result
    }

    fun updateContacto(identifier: Int, newName: String, newTlf: String) {
        val args = arrayOf(identifier.toString())
        // Se crea un ArrayMap<>() con los datos nuevos.
        val data = ContentValues()
        data.put(COLUMNA_NOMBRE, newName)
        data.put(COLUMNA_TELEFONO, newTlf)
        val db = this.writableDatabase
        db.update(TABLA_CONTACTOS, data, "$COLUMNA_ID = ?", args)
        db.close()
    }

    /**
     * Método para devolver un texto con todos los elementos de la tabla
    recorridos por un cursor
     Habria que ir ahora main activity a cambiar el metodo consultar en los botones
     */
    fun mostrarContactos(): String {
        var texto = ""
        // Se instancia la BD en modo lectura y se crea la SELECT.
        val db: SQLiteDatabase = this.readableDatabase
        val cursor: Cursor = db.rawQuery(
            "SELECT * FROM ${TABLA_CONTACTOS};",
            null
        )
        // Se obtienen los índices de las columnas.
        // Recogemos el número de columna de cada campo, en vez de ponerlos
        val numColumId = cursor.getColumnIndex(COLUMNA_ID)
        val numColumNombre = cursor.getColumnIndex(COLUMNA_NOMBRE)
        val numColumTlf = cursor.getColumnIndex(COLUMNA_TELEFONO)
        // Se comprueba que al menos exista un registro.
        if (cursor.moveToFirst()) {
            do {
                texto += cursor.getInt(numColumId).toString() + " - "
                texto += cursor.getString(numColumNombre).toString() + " "
                texto += cursor.getString(numColumTlf).toString() + "\n"
            } while (cursor.moveToNext())
        } else {
            texto = "No existen datos a mostrar."
        }
        db.close()
        // Se cierra el cursor.
        return texto
    }
}

