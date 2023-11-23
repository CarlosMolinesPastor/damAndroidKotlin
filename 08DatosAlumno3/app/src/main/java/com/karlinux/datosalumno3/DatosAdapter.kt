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
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.karlinux.datosalumno3.databinding.ItemAlumnoBinding

//Creamos el adapter ahora con la base de datos le pasamos el contexto y el cursor en lugar de la lista
class DatosAdapter(context:Context, cursor: Cursor) : RecyclerView.Adapter<DatosAdapter.DatosViewHolder>(){
    // Creamos las variables para el cursor y el contexto.
    private lateinit var binding: ItemAlumnoBinding
    var mycursor : Cursor
    var myContext : Context
    //Iniciamos las variables
    init {
        mycursor = cursor
        myContext = context
    }
    //Creamos un Companion Object para poder acceder a la lista desde la clase VistaActivity
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

    //Creamos el ViewHolder con los elementos de la UI inflamos la vista y la devolvemos en el ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DatosViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_alumno, parent, false)
        return DatosViewHolder(view)
    }
    //Enlazamos el ViewHolder con los datos de la base de datos a traves del cursor
    override fun onBindViewHolder(holder: DatosViewHolder, position: Int) {
        mycursor.moveToPosition(position)
        holder.bindData(mycursor)
    }
    //Devolvemos el tamaño de la lista por el numero de elementos del cursor
    override fun getItemCount(): Int {
        return mycursor.count
    }
    //
    inner class DatosViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding: ItemAlumnoBinding = ItemAlumnoBinding.bind(view)
        // Inicializar el listener para el evento onClick. Esto es para que no haya
        // problemas con el click, ya que el RecyclerView no tiene un listener propio.
        //El ViewHolder reutiliza las vistas y cuando se creaba me daba problemas porque el click solamente marcaba la ultima posicion de la vista
        // por lo he buscado y he encontrado una solucion: he utilizado un  clic general para cualquier elemento en el RecyclerView,
        // asi apunta el cursor actualizado y apunta al último elemento, lo que hace que los clicks lleven al mismo resultado.
        init {
            view.setOnClickListener {
                //Para ver que funciona y que se pulsa el elemento
                Log.d(TAG_APP, "Elemento $adapterPosition clicado.")
                // Se obtiene la posición del elemento que se ha pulsado.
                val position: Int = adapterPosition
                // Se comprueba que la posición sea válida.
                if (position != RecyclerView.NO_POSITION) {
                    // Se mueve el cursor a la posición del elemento pulsado.
                    mycursor.moveToPosition(position)
                    // Se crea el intent con los datos del alumno.
                    val intent = Intent(it.context, VistaActivity::class.java).apply {
                        putExtra(EXTRA_ID, mycursor.getString(0))
                        putExtra(EXTRA_NOMBRE, mycursor.getString(1))
                        putExtra(EXTRA_DIA, mycursor.getString(2))
                        putExtra(EXTRA_MES, mycursor.getString(3))
                        putExtra(EXTRA_ANO, mycursor.getString(4))
                        putExtra(EXTRA_MODALIDAD, mycursor.getString(5))
                        putExtra(EXTRA_CICLO, mycursor.getString(6))
                        putExtra(EXTRA_GRUPOCLASE,mycursor.getString(7))
                    }
                    // Se lanza el intent.
                    startActivity(it.context, intent, null)
                }
            }
        }
        //Funcion para enlazar los datos del cursor con los elementos de la UI
        fun bindData(cursor: Cursor) {
            binding.apply {
                labelNombre.text = cursor.getString(1)
                labeDia.text = cursor.getString(2)
                labelMes.text = cursor.getString(3)
                labelAnyo.text = cursor.getString(4)
                labelModalidad.text = cursor.getString(5)
                labelCiclo.text = cursor.getString(6)
            }
        }
    }
}
