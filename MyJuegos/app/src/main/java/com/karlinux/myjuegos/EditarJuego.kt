package com.karlinux.myjuegos

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.bumptech.glide.Glide
import com.karlinux.myjuegos.databinding.ActivityEditarJuegoBinding

class EditarJuego : AppCompatActivity() {
    private lateinit var binding: ActivityEditarJuegoBinding
    private lateinit var datosDBHelper: MyDataDBOpenHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    binding = ActivityEditarJuegoBinding.inflate(layoutInflater)
    setContentView(binding.root)

        // Instanciamos el objeto MyDBOpenHelper.
        datosDBHelper = MyDataDBOpenHelper(this)

        val id = intent.getIntExtra("id", 0)
        val nombre = intent.getStringExtra("nombre")
        val desarrollador = intent.getStringExtra("desarrollador")
        val anyo = intent.getStringExtra("anyo")
        val imagen = intent.getStringExtra("imagen")

        binding.edittxtNombre.text = Editable.Factory.getInstance().newEditable(nombre)
        binding.editxtDesarrollador.text = Editable.Factory.getInstance().newEditable(desarrollador)
        binding.edittxtAnyo.text = Editable.Factory.getInstance().newEditable(anyo)
        binding.edittxtImagen.text = Editable.Factory.getInstance().newEditable(imagen)

        binding.btnAnyadir.setOnClickListener {
            val nuevoNombre = binding.edittxtNombre.text.toString()
            val nuevoDesarrollador = binding.editxtDesarrollador.text.toString()
            val nuevoAnyo = binding.edittxtAnyo.text.toString()
            val nuevaImagen = binding.edittxtImagen.text.toString()
            datosDBHelper.updateJuego(id,nuevoNombre, nuevoDesarrollador, nuevoAnyo, nuevaImagen)
            // Crear un Intent para volver al MainActivity pero para ver la lista de juegos
            val intent = Intent(this@EditarJuego, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            //Señal para pasarsela al MainActivity para indicarle que venimos de aqui
            intent.putExtra("verLista", true)
            startActivity(intent)
            finish()
        }
        binding.btnCancel.setOnClickListener {
            finish()
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

    }
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
            binding.imageView.setImageResource(R.drawable.android)
        }
    }
    private fun buscarEnInternet() {
        val nombreJuego = binding.edittxtNombre.text.toString()
        if (nombreJuego.isEmpty()){
            Toast.makeText(this, "Faltan datos para buscar en Internet", Toast.LENGTH_SHORT)
                .show()
            return
        } else {
                val intentDefault = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?tbm=isch&q=$nombreJuego game"))
                startActivity(intentDefault)
        }
    }
}