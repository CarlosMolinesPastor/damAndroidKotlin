// ############# BASE DE DATOS #################
package com.karlinux.datosalumno3

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class MyDatosDBOpenHelper (context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    val TAG = "SQLite"

    companion object {
        val DATABASE_VERSION = 1
        val DATABASE_NAME = "datos.db"
        val TABLA_ALUMNOS = "alumnos"
    }

    val COLUMNA_ID = "_id"
    val COLUMNA_NOMBRE = "nombre"
    val COLUMNA_DIA = "dia"
    val COLUMNA_MES = "mes"
    val COLUMNA_ANO = "ano"
    val COLUMNA_MODALIDAD = "modalidad"
    val COLUMNA_CICLO = "ciclo"
    val COLUMNA_GRUPOCLASE = "grupoClase"
    override fun onCreate(db: SQLiteDatabase?) {
        try {
            val crearTablaAlumnos = "CREATE TABLE $TABLA_ALUMNOS " +
                    "($COLUMNA_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMNA_NOMBRE TEXT, " +
                    "$COLUMNA_DIA TEXT, " +
                    "$COLUMNA_MES TEXT, " +
                    "$COLUMNA_ANO TEXT, " +
                    "$COLUMNA_MODALIDAD TEXT, " +
                    "$COLUMNA_CICLO TEXT, " +
                    "$COLUMNA_GRUPOCLASE TEXT)"
            db!!.execSQL(crearTablaAlumnos)
        } catch (e: SQLiteException) {
            Log.e("$TAG (onCreate)", e.message.toString())
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        try {
            val dropTablaAlumnos = "DROP TABLE IF EXISTS $TABLA_ALUMNOS"
            db!!.execSQL(dropTablaAlumnos)
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

    //CRUD ###########

    //CREATE
    fun addAlumno(
        nombre: String, dia: String, mes: String, ano: String,
        modalidad: String, ciclo: String, grupoClase: String
    ) {
        try {
            val data = ContentValues()
            data.put(COLUMNA_NOMBRE, nombre)
            data.put(COLUMNA_DIA, dia)
            data.put(COLUMNA_MES, mes)
            data.put(COLUMNA_ANO, ano)
            data.put(COLUMNA_MODALIDAD, modalidad)
            data.put(COLUMNA_CICLO, ciclo)
            data.put(COLUMNA_GRUPOCLASE, grupoClase)
            // Se abre la BD en modo escritura.
            val db = this.writableDatabase
            db.insert(TABLA_ALUMNOS, null, data)
            db.close()
        } catch (e: SQLiteException) {
            Log.e("$TAG (insertarAlumno)", e.message.toString())
        }
    }

    //Ni el metodo borrarAlumno (delAlumno) ni el metodo updateAlumno (updateAlumno) se usan pero
    // los dejo por si acaso
    fun delAlumno(identifier: Int): Int {
        val args = arrayOf(identifier.toString())
        // Se abre la BD en modo escritura.
        val db = this.writableDatabase
        // Se puede elegir un sistema u otro.
        val result = db.delete(TABLA_ALUMNOS, "$COLUMNA_ID = ?", args)
        // db.execSQL("DELETE FROM $TABLA_CONTACTOS WHERE $COLUMNA_ID = ?", args)
        db.close()
        return result
    }

    fun updateAlumno(identifier: Int, newName: String, newDia: String, newMes: String,
                     newAno: String, newModalidad: String, newCiclo: String, newGrupoClase: String) {
        val args = arrayOf(identifier.toString())
        // Se crea un ArrayMap<>() con los datos nuevos.
        val data = ContentValues()
        data.put(COLUMNA_NOMBRE, newName)
        data.put(COLUMNA_DIA, newDia)
        data.put(COLUMNA_MES, newMes)
        data.put(COLUMNA_ANO, newAno)
        data.put(COLUMNA_MODALIDAD, newModalidad)
        data.put(COLUMNA_CICLO, newCiclo)
        data.put(COLUMNA_GRUPOCLASE, newGrupoClase)
        // Se abre la BD en modo escritura.
        val db = this.writableDatabase
        db.update(TABLA_ALUMNOS, data, "$COLUMNA_ID = ?", args)
        db.close()
    }
}
