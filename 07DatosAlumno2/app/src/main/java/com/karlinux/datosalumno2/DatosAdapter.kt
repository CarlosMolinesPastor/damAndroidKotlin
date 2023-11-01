package com.karlinux.datosalumno2

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DatosAdapter(datoslist: MutableList<Datos>, context:Context) : RecyclerView.Adapter<DatosAdapter.DatosViewHolder>(){
    // Creamos las variables primero una lista mutable de la data class datos y el contexto
    var myDatos : MutableList<Datos>
    var myContext : Context
    //Iniciamos las variables
    init {
        myDatos = datoslist
        myContext = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DatosViewHolder {
    //Inflamos el layout item_alumno
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_alumno, parent, false)
        return DatosViewHolder(view)
    }

    override fun onBindViewHolder(holder: DatosViewHolder, position: Int) {
        val item = myDatos.get(position)
        holder.bind(item, myContext)
    }

    override fun getItemCount(): Int {
        return myDatos.size
    }

    //Hay que crear la clase ViewHolder
    class DatosViewHolder(view: View) : RecyclerView.ViewHolder(view){
        // Aquí es necesario utilizar findViewById para localizar el elemento
        // de la vista que se pasa como parámetro.
        private val nombre = view.findViewById(R.id.labelNombre) as TextView
        private val diaNac = view.findViewById(R.id.labeDia) as TextView
        private val modalidad = view.findViewById(R.id.labelModalidad) as TextView
        private val ciclo = view.findViewById(R.id.labelCiclo) as TextView

        // Creamos la función bind que recibe un objeto de la clase Datos y el contexto
        fun bind(datos: Datos, context: Context){
            Log.d("bind", datos.nombre.toString())

            nombre.text = datos.nombre
            diaNac.text = datos.fecha
            modalidad.text = datos.modalidad
            ciclo.text = datos.ciclo

            // Definimos el código a ejecutar si se hace click en el item
            //itemView.setOnClickListener {
                //Toast.makeText(context,datos.grupoClase.toString(),Toast.LENGTH_SHORT).show()
            //}
        }

    }
}