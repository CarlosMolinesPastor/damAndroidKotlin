package com.carmolpas.myexplicitintent

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.carmolpas.myexplicitintent.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySecondBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mytitulo = intent.getStringExtra(MainActivity.EXTRA_TITULO)
        val myautor = intent.getStringExtra(MainActivity.EXTRA_AUTOR)
        //binding.txtDatos.text = "Titulo: " + mytitutlo + "\nAutor: " + myautor
        binding.txtDatos.text = "Titulo: $mytitulo \nAutor: $myautor"


        // Cremos los listeners
        //Importante ha de llevar el finish para decirle que efectivamente se finaliza actividad
        binding.btAceptar.setOnClickListener() {
            val intentResult :Intent = Intent().apply {
            //Metemos los datos en el MainActivity
                putExtra(MainActivity.EXTRA_COMENTARIO, binding.editOpinion.text.toString())
                putExtra(MainActivity.EXTRA_RATING, binding.ratingBar.rating)
           }

            Log.d(MainActivity.TAG_APP, "Pulsado boton Aceptar")
            setResult(Activity.RESULT_OK, intentResult) // Importante meter el intent para pasarlo
            finish()
        }
        binding.btCancelar.setOnClickListener() {
            Log.d(MainActivity.TAG_APP, "Se ha pulsado Cancelar")
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }

}