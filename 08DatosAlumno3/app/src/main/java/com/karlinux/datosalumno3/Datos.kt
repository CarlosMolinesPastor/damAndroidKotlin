//######  DATA CLASS #########
//Creamos el data class para poder crear la lista que le pasamos



//###### Ojo ######## Con la base de datos ya no es necesario crear la lista de datos



package com.karlinux.datosalumno3

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