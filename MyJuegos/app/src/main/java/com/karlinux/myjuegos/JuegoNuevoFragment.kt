package com.karlinux.myjuegos

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.karlinux.myjuegos.databinding.FragmentJuegoNuevoBinding
import java.net.URLEncoder

class JuegoNuevoFragment : Fragment() {
    //## PRIMERO BINDING
    private lateinit var binding: FragmentJuegoNuevoBinding
    private lateinit var datosDBHelper: MyDataDBOpenHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle? ): View? {
        //Menu, primero para que se muestre
        setHasOptionsMenu(true)

        // Obtener la actividad que contiene este fragmento
        val activity = requireActivity() as AppCompatActivity
        // Cambiar el color de la barra de herramientas
        activity.supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#0acef5")))

        // Inflate the layout for this fragment
        binding = FragmentJuegoNuevoBinding.inflate(inflater)

        // Instanciamos el objeto MyDBOpenHelper.
        datosDBHelper = MyDataDBOpenHelper(requireContext())

        binding.btnAnyadir.setOnClickListener() {
            ocultarTeclado(requireView())
            myAlertDialog()
        }
        binding.btnCancel.setOnClickListener() {
            ocultarTeclado(requireView())
            binding.edittxtNombre.requestFocus()
        }
        // Agregar un TextWatcher al EditText para cargar la imagen cuando el texto cambie
        binding.edittxtImagen.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                cargarImagenDesdeEditText()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No es necesario implementar nada aquí
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // No es necesario implementar nada aquí
            }
        })

        binding.imageView.setOnClickListener {
            buscarEnInternet()
        }

        // Cargar una imagen predeterminada o la imagen de la URL si ya está presente
        cargarImagenDesdeEditText()
        return binding.root
    }

    private fun buscarEnInternet() {
        val nombreJuego = binding.edittxtNombre.text.toString()
        if (nombreJuego.isEmpty()){
            ocultarTeclado(binding.edittxtNombre)
            Toast.makeText(context, "Faltan datos en 'Nombre del Juego' para buscar en Internet", Toast.LENGTH_SHORT)
                .show()
            return
        } else {
            // Se crea un intent para buscar en Internet con el nombre del juego y (Videojuego) para que busque imagenes de videojuegos
            val intentDefault = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?tbm=isch&q=$nombreJuego (Videojuego)"))
            ocultarTeclado(binding.edittxtNombre)
            startActivity(intentDefault)
        }
    }
    private fun introducirDatos() {
        val nombre = binding.edittxtNombre.text.toString()
        val desarrollador = binding.editxtDesarrollador.text.toString()
        val anyo = binding.edittxtAnyo.text.toString()
        val imagen = binding.edittxtImagen.text.toString()

        if (nombre.isEmpty() || desarrollador.isEmpty() || anyo.isEmpty() || imagen.isEmpty()) {
            makeText(activity, "Faltan datos a guardar", Toast.LENGTH_SHORT).show()
        } else if (datosDBHelper.juegoYaExiste(nombre, desarrollador)) {
            makeText(activity, "El juego ya existe con ese desarrollador", Toast.LENGTH_SHORT).show()
            binding.edittxtNombre.text.clear()
            binding.editxtDesarrollador.text.clear()
        } else {
            datosDBHelper.addJuego(nombre, desarrollador, anyo, imagen)
            makeText(activity, "Datos guardados en la base de datos", Toast.LENGTH_SHORT).show()
            borrarDatos()
        }
    }
    private fun borrarDatos()
    {
        binding.edittxtNombre.setText("")
        binding.editxtDesarrollador.setText("")
        binding.edittxtAnyo.setText("")
        binding.edittxtImagen.setText("")
    }

    fun ocultarTeclado(view: View) {
        val inputMethodManager = requireContext().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    //Vale vamos a hacer la funcion de el cuadro de alerta
    private fun myAlertDialog() {
        val builder = AlertDialog.Builder(requireContext())
        // Se crea el AlertDialog
        builder.apply {
            // Se asigna un título al AlertDialog.
            setTitle("BASE DE DATOS")
            // Se asgina el cuerpo del mensaje
            setMessage("Desea guardar la informacion obtenida en la BD?")
            // Se define el comportamiento de los botones
            // Se define el comportamiento del botón positivo
            setPositiveButton(
                android.R.string.ok,
                DialogInterface.OnClickListener(function = actionButton)
            )
            // Se define el comportamiento del botón negativo
            setNegativeButton(
                android.R.string.cancel,
                DialogInterface.OnClickListener(function = actionButton2))

        }
        // Se muestra el AlertDialog.
        builder.show()
    }
    // Declaramos las funciones de los botones del cuadro de alerta
    private val actionButton = { dialog: DialogInterface, which: Int ->
        //makeText(requireContext(), android.R.string.ok, Toast.LENGTH_SHORT).show()
        //Llamamos a la funcion introducir datos
        introducirDatos()

    }
    private val actionButton2 = { dialog: DialogInterface, which: Int ->
        makeText(requireContext(), android.R.string.cancel, Toast.LENGTH_SHORT).show()
        //Llamamos a la funcion borrar datos
        borrarDatos()
    }

    // Cargar una imagen predeterminada o la imagen de la URL si ya está presente, o una imagen de error si no se puede cargar
    // Las imagenes las he introducido en drawable y las he llamado desde el codigo. Despues he modificado el glide para que las cargue
    // desde drawable.
    private fun cargarImagenDesdeEditText() {
        val imageUrl = binding.edittxtImagen.text.toString()
        if (imageUrl.isNotEmpty()) {
            Glide.with(this) // 'this' se refiere al Fragment
                .load(imageUrl)
                .fitCenter()
                .placeholder(R.drawable.placeholder) // Imagen de placeholder mientras carga la imagen
                .error(R.drawable.error) // Imagen por defecto si hay un error al cargar
                .into(binding.imageView)
        } else {
            // Cargar una imagen predeterminada de Android si el campo está vacío
            binding.imageView.setImageResource(R.drawable.busqueda
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menuanyadir -> {
                ocultarTeclado(requireView())
                myAlertDialog()
                true
            }
            R.id.menueliminar -> {
                ocultarTeclado(requireView())
                binding.edittxtNombre.requestFocus()
                borrarDatos()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}