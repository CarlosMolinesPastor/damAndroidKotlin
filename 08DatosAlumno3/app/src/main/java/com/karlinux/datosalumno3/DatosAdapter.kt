//####   ADAPTADOR PARA EL RECYCLERVIEW   ####
package com.karlinux.datosalumno3

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.karlinux.datosalumno3.databinding.ItemAlumnoBinding

//Creamos el adapter
class DatosAdapter(context:Context, cursor: Cursor) : RecyclerView.Adapter<DatosAdapter.DatosViewHolder>(){
    // Creamos las variables primero una lista mutable de la data class datos y el contexto
    private lateinit var binding: ItemAlumnoBinding
    var mycursor : Cursor
    var myContext : Context
    //Creamos un Companion Object para poder acceder a la lista desde la clase VistaActivity

    //Iniciamos las variables
    init {
        mycursor = cursor
        myContext = context
    }

    companion object {
        const val TAG_APP = "myDatosPersonales"

        // Lo que envia
        const val EXTRA_NOMBRE = "myNombre"

        // Los que recibe
        const val EXTRA_ID = "myId"
        const val EXTRA_DIA = "myDia"
        const val EXTRA_MES = "myMes"
        const val EXTRA_ANO = "myAno"
        const val EXTRA_MODALIDAD = "myModalidad"
        const val EXTRA_CICLO = "myCiclo"
        const val EXTRA_GRUPOCLASE = "myGrupoClase"
    }

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int ): DatosViewHolder {
        Log.d("RECYCLERVIEW", "onCreateViewHolder")
        val inflater = LayoutInflater.from(parent.context)
        return DatosViewHolder(
            inflater.inflate(R.layout.item_alumno,parent,false)
        )
    }

    //Enlazamos el ViewHolder con los datos
    override fun onBindViewHolder(holder: DatosViewHolder, position: Int) {
        // Importante para recorrer el cursor.
        mycursor.moveToPosition(position)
        Log.d("RECYCLERVIEW", "onBindViewHolder")
        // Se asignan los valores a los elementos de la UI.
        holder.nombre.text= mycursor.getString(1)
        holder.dia.text = mycursor.getString(2)
        holder.mes.text = mycursor.getString(3)
        holder.ano.text = mycursor.getString(4)
        holder.modalidad.text = mycursor.getString(5)
        holder.ciclo.text = mycursor.getString(6)
    }

    //Devolvemos el tamaño de la lista
    override fun getItemCount(): Int {
        return if (mycursor != null)
            mycursor.count
        else 0
    }

    //Hay que crear la clase ViewHolder que extiende de RecyclerView.ViewHolder
    inner class DatosViewHolder : RecyclerView.ViewHolder {

        val nombre: TextView
        val dia: TextView
        val mes: TextView
        val ano: TextView
        val modalidad: TextView
        val ciclo: TextView

        constructor(view: View) : super(view) {
            // Se enlazan los elementos de la UI mediante ViewBinding.
            val bindingIAlum = ItemAlumnoBinding.bind(view)
            this.nombre = bindingIAlum.labelNombre
            this.dia = bindingIAlum.labeDia
            this.mes = bindingIAlum.labelMes
            this.ano = bindingIAlum.labelAnyo
            this.modalidad = bindingIAlum.labelModalidad
            this.ciclo = bindingIAlum.labelCiclo

            itemView.setOnClickListener {
                //Toast.makeText(context,datos.grupoClase.toString(), Toast.LENGTH_SHORT).show()
                val myIntent: Intent = Intent(it?.context, VistaActivity::class.java).apply {
                    putExtra(EXTRA_ID, mycursor.getString(0).toString())
                    putExtra(EXTRA_NOMBRE, mycursor.getString(1).toString())
                    putExtra(EXTRA_DIA, mycursor.getString(2).toString())
                    putExtra(EXTRA_MES, mycursor.getString(3).toString())
                    putExtra(EXTRA_ANO, mycursor.getString(4).toString())
                    putExtra(EXTRA_MODALIDAD, mycursor.getString(5).toString())
                    putExtra(EXTRA_CICLO, mycursor.getString(6).toString())
                    putExtra(EXTRA_GRUPOCLASE,mycursor.getString(7).toString())
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
