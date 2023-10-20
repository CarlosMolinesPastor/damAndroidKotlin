package com.carmolpas.myexplicitintent

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.carmolpas.myexplicitintent.MainActivity.Companion.EXTRA_AUTOR
import com.carmolpas.myexplicitintent.MainActivity.Companion.EXTRA_TITULO
import com.carmolpas.myexplicitintent.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    //Creamos un companion object para pasarle todos los datos a la segunda actividad
    // Serian tres variables 1 el tag para controlar el Log cat y las otras dos para los dos
    // campos, tener en cuenta que si pusieramos tipo string despues tendriamos muchos problemas
    // De aqui nos vamos al interior d la funcion onCreate para pasarle los datos con el intent
    companion object {
        const val TAG_APP = "myExplicitIntent"
        const val EXTRA_TITULO = "myTitulo"
        const val EXTRA_AUTOR = "myAutor"
        const val EXTRA_COMENTARIO = "myComentario"
        const val EXTRA_RATING = "myRating"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btEnviar.setOnClickListener() {
            lanzarSegundaActividad()
        }
    }

    private fun lanzarSegundaActividad() {
        // Se crea un objeto de tipo Intent
        val myIntent = Intent(this, SecondActivity::class.java).apply {
            // Se añade la información a pasar por clave-valor
            putExtra(EXTRA_AUTOR, binding.editAutor.text.toString())
            putExtra(EXTRA_TITULO, binding.editTitulo.text.toString())
        }
        getResult.launch(myIntent)

    }

    private val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val myComentario = it.data?.getStringExtra(EXTRA_COMENTARIO)
            val myRating = it.data?.getFloatExtra(EXTRA_RATING,0.0F).toString()

            binding.txtResult.text = "Valoracion del Usuario: \nRating = " + myRating + "\nComentario:\n"+  myComentario
            binding.txtResult.visibility = View.VISIBLE
        }
        if (it.resultCode == Activity.RESULT_CANCELED)
        {
            binding.txtResult.text = "Se ha cancelado"
            binding.txtResult.visibility = View.VISIBLE
        }
    }
}
