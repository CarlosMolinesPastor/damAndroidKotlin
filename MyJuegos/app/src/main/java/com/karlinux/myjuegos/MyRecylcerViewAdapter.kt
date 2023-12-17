package com.karlinux.myjuegos

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.provider.Settings.Global.getString
import android.util.Log
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.karlinux.myjuegos.databinding.ItemBinding


class MyRecyclerViewAdapter(context: Context, cursor: Cursor)
    : RecyclerView.Adapter<MyRecyclerViewAdapter.DatosViewHolder>() {
    //Definimos una interfaz en el adaptador


    private lateinit var bindingItemsRV: ItemBinding
    val context: Context
    var mycursor: Cursor

    companion object{
        const val TAG_APP = "myJuegos"
        const val COLUMNA_ID = "id"
        const val COLUMNA_NOMBRE = "nombre"
        const val COLUMNA_DESARROLLADOR = "desarrollador"
        const val COLUMNA_ANYO = "anyo"
        const val COLUMNA_IMAGEN = "imagen"
    }
    init {
        this.context = context
        this.mycursor = cursor
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DatosViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item, parent, false)
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
        private val binding: ItemBinding = ItemBinding.bind(view)
        var currentPosition: Int = 0 //Almacena la posicion actual del cursor
        // Inicializar el listener para el evento onClick. Esto es para que no haya
        // problemas con el click, ya que el RecyclerView no tiene un listener propio.
        //El ViewHolder reutiliza las vistas y cuando se creaba me daba problemas porque el click solamente marcaba la ultima posicion de la vista
        // por lo he buscado y he encontrado una solucion: he utilizado un  clic general para cualquier elemento en el RecyclerView,
        // asi apunta el cursor actualizado y apunta al último elemento, lo que hace que los clicks lleven al mismo resultado.
        //
        //Para resolver este problema, puedes cambiar la lógica del OnClickListener para que capture los datos del elemento específico en el que se hizo clic.
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
                    val intent = Intent(it.context, Vista::class.java).apply {
                        //Lo paso como string porque el cursor devuelve un string
                        putExtra(COLUMNA_ID, mycursor.getString(0).toString())
                        putExtra(COLUMNA_NOMBRE, mycursor.getString(1))
                        putExtra(COLUMNA_DESARROLLADOR, mycursor.getString(2))
                        putExtra(COLUMNA_ANYO, mycursor.getString(3))
                        putExtra(COLUMNA_IMAGEN, mycursor.getString(4))
                    }
                    // Se lanza el intent.
                    ContextCompat.startActivity(it.context, intent, null)
                }
            }
        }
        init{
            view.setOnLongClickListener(View.OnLongClickListener {
                val builder = android.app.AlertDialog.Builder(context)
                builder.setMessage("¿Esta seguro que desea eliminar el juego?")
                    .setCancelable(false)
                    .setPositiveButton("Sí") { dialog, id ->
                        val datosDBHelper: MyDataDBOpenHelper = MyDataDBOpenHelper(context)
                        mycursor.moveToPosition(currentPosition)
                        datosDBHelper.delJuego(mycursor.getInt(0))
                        Toast.makeText(context, "Juego eliminado", Toast.LENGTH_SHORT).show()
                        notifyDataSetChanged()
                        volverListaJuegos()
                    }
                    .setNegativeButton("No") { dialog, id ->
                        dialog.cancel()
                    }
                val alert = builder.create()
                alert.show()
                true
            })
        }
        //Funcion para enlazar los datos del cursor con los elementos de la UI
        fun bindData(cursor: Cursor) {
            currentPosition = cursor.position //Almacena la posicion actual del cursor
            binding.apply {
                labelNombre.text = cursor.getString(1)
                labelDesarrollador.text = cursor.getString(2)
                labelAnyo.text = cursor.getString(3)
                val imageUrl = cursor.getString(4)
                // Uso Glide carga la imagen desde la URL en el ImageView
                Glide.with(itemView.context)
                    .load(imageUrl)
                    .into(labelImagen)
            }
        }
        //Funcion para volver a la lista de juegos y que se actualice la lista al eliminar un juego
        fun volverListaJuegos(){
            //Creamos un intent para volver al MainActivity
            val intent = Intent(context, MainActivity::class.java)
            //Se le añade la bandera para que no se quede en la activity anterior
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            //Señal para pasarsela al MainActivity para indicarle que venimos de aqui
            intent.putExtra("verLista", true)
            context.startActivity(intent)
        }
    }
}