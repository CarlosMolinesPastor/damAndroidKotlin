//Creamos el data class para poder crear la lista que le pasamos
package com.karlinux.datosalumno2

//Creamos la data class Datos
data class Datos (
    val dia :String,
    val mes: String,
    val ano :String,
    val nombre: String,
    val modalidad: String,
    val ciclo: String,
    val grupoClase: String
)