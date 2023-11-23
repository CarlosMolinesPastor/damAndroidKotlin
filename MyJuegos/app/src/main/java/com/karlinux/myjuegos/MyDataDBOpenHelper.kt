package com.karlinux.myjuegos

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class MyDataDBOpenHelper (context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    val TAG = "SQLite"
    // Se definen las constantes de la BD. con companion object se puede acceder a ellas desde cualquier sitio
    companion object {
        val DATABASE_VERSION = 1
        val DATABASE_NAME = "datos.db"
        val TABLA_JUEGOS = "juegos"
    }

    val COLUMNA_ID = "_id"
    val COLUMNA_NOMBRE = "nombre"
    val COLUMNA_DESARROLLADOR = "desarrollador"
    val COLUMNA_ANYO = "anyo"
    val COLUMNA_IMAGEN = "imagen"

    // Se crea la tabla de la BD
    override fun onCreate(db: SQLiteDatabase?) {
        try {
            val crearTablaJuegos = "CREATE TABLE $TABLA_JUEGOS " +
                    "($COLUMNA_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMNA_NOMBRE TEXT, "  +
                    "$COLUMNA_DESARROLLADOR TEXT, " +
                    "$COLUMNA_ANYO TEXT, " +
                    "$COLUMNA_IMAGEN TEXT)"
            db!!.execSQL(crearTablaJuegos)
        } catch (e: SQLiteException) {
            Log.e("$TAG (onCreate)", e.message.toString())
        }
    }

    // Se actualiza la tabla de la BD
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        try {
            val dropTablaJuegos = "DROP TABLE IF EXISTS $TABLA_JUEGOS"
            db!!.execSQL(dropTablaJuegos)
            onCreate(db)
        } catch (e: SQLiteException) {
            Log.e("$TAG (onUpgrade)", e.message.toString())
        }
    }

    override fun onOpen(db: SQLiteDatabase?) {
        super.onOpen(db)
        Log.d("$TAG (onOpen)", "¡¡Base de datos abierta!!")
    }

    // ################ CRUD #################

    fun addJuego(
        nombre: String, desarrollador: String, anyo: String,
        imagen: String
    ) {
        try {
            val data = ContentValues()
            data.put(COLUMNA_NOMBRE, nombre)
            data.put(COLUMNA_DESARROLLADOR, desarrollador)
            data.put(COLUMNA_ANYO, anyo)
            data.put(COLUMNA_IMAGEN, imagen)
            // Se abre la BD en modo escritura.
            val db = this.writableDatabase
            db.insert(TABLA_JUEGOS, null, data)
            db.close()
        } catch (e: SQLiteException) {
            Log.e("$TAG (insertarJuego)", e.message.toString())
        }
    }

    fun delJuego(identifier: Int): Int {
        val args = arrayOf(identifier.toString())
        // Se abre la BD en modo escritura.
        val db = this.writableDatabase
        // Se puede elegir un sistema u otro.
        val result = db.delete(TABLA_JUEGOS, "$COLUMNA_ID = ?", args)
        // db.execSQL("DELETE FROM $TABLA_CONTACTOS WHERE $COLUMNA_ID = ?", args)
        db.close()
        return result
    }
    //UPDATE
    fun updateJuego(identifier: Int, newName: String, newDesarrollador: String, newAnyo: String,
                    newImagen: String) {
        val args = arrayOf(identifier.toString())
        // Se crea un ArrayMap<>() con los datos nuevos.
        val data = ContentValues()
        data.put(COLUMNA_NOMBRE, newName)
        data.put(COLUMNA_DESARROLLADOR, newDesarrollador)
        data.put(COLUMNA_ANYO, newAnyo)
        data.put(COLUMNA_IMAGEN, newImagen)
        // Se abre la BD en modo escritura.
        val db = this.writableDatabase
        db.update(TABLA_JUEGOS, data, "$COLUMNA_ID = ?", args)
        db.close()
    }

    //funcion para cmprobar que el juego no existe ya en la base de
    // datos y no se repita con el desarrollador
    fun juegoYaExiste(nombre: String, desarrollador: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLA_JUEGOS,
            arrayOf(COLUMNA_ID),
            "$COLUMNA_NOMBRE = ? AND $COLUMNA_DESARROLLADOR = ?",
            arrayOf(nombre, desarrollador),
            null,
            null,
            null
        )
        val existe = cursor.count > 0
        cursor.close()
        db.close()
        return existe
    }
}
