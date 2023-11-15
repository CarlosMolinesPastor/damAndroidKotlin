package com.karlinux.datosalumno3

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.karlinux.datosalumno3.databinding.FragmentHistoricoBinding
import com.karlinux.datosalumno3.databinding.ItemAlumnoBinding

//Creamos el adapter
class DatosAdapter(datoslist: MutableList<Datos>, context:Context) : RecyclerView.Adapter<DatosAdapter.DatosViewHolder>(){
    // Creamos las variables primero una lista mutable de la data class datos y el contexto
    private lateinit var binding: ItemAlumnoBinding
    var myDatos : MutableList<Datos>
    var myContext : Context
    //Creamos un Companion Object para poder acceder a la lista desde la clase VistaActivity

    //Iniciamos las variables
    init {
        myDatos = datoslist
        myContext = context
    }

    companion object {
        const val TAG_APP = "myDatosPersonales"

        // Lo que envia
        const val EXTRA_NOMBRE = "myNombre"

        // Los que recibe
        const val EXTRA_DIA = "myDia"
        const val EXTRA_MES = "myMes"
        const val EXTRA_ANO = "myAno"
        const val EXTRA_MODALIDAD = "myModalidad"
        const val EXTRA_CICLO = "myCiclo"
        const val EXTRA_GRUPOCLASE = "myGrupoClase"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DatosViewHolder {
    //Inflamos el layout item_alumno y lo pasamos al viewholder
        val binding = ItemAlumnoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DatosViewHolder(binding)
    }

    //Enlazamos el ViewHolder con los datos
    override fun onBindViewHolder(holder: DatosViewHolder, position: Int) {
        val item = myDatos.get(position)
        holder.bind(item, myContext)
    }

    //Devolvemos el tamaño de la lista
    override fun getItemCount(): Int {
        return myDatos.size
    }

    //Hay que crear la clase ViewHolder que extiende de RecyclerView.ViewHolder
    class DatosViewHolder(private val binding: ItemAlumnoBinding) : RecyclerView.ViewHolder(binding.root) {
        // Creamos la función bind que recibe un objeto de la clase Datos y el contexto
        fun bind(datos: Datos, context: Context){
            Log.d("bind", datos.nombre.toString())
            binding.apply {
                labelNombre.text = datos.nombre
                labeDia.text = datos.dia
                labelMes.text = datos.mes
                labelAnyo.text = datos.ano
                labelModalidad.text = datos.modalidad
                labelCiclo.text = datos.ciclo
            }

            // Definimos el código a ejecutar si se hace click en el item
            itemView.setOnClickListener {
                //Toast.makeText(context,datos.grupoClase.toString(), Toast.LENGTH_SHORT).show()
                val myIntent: Intent = Intent(it?.context, VistaActivity::class.java).apply {
                    putExtra(EXTRA_GRUPOCLASE, datos.grupoClase.toString())
                    putExtra(EXTRA_NOMBRE, datos.nombre.toString())
                    putExtra(EXTRA_DIA, datos.dia.toString())
                    putExtra(EXTRA_MES,datos.mes.toString())
                    putExtra(EXTRA_ANO,datos.ano.toString())
                    putExtra(EXTRA_MODALIDAD,datos.modalidad.toString())
                    putExtra(EXTRA_CICLO,datos.ciclo.toString())
                }
                //Lanzamos el intent
                // it se refiere a la vista (itemView) dentro de la lambda.
                // context es el contexto de la aplicación de Android que se pasa
                // como parámetro a la función bind.
                it?.context?.startActivity(myIntent)
            }

        }

    }
}