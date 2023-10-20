package com.karlinux.mybookopinion

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.karlinux.mybookopinion.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        // Estas dos líneas sustituyen a setContentView()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btHecho.setOnClickListener{
            mostrarOpinion(it)
        }

        binding.txtOpinion.setOnClickListener{
            volverEditOpinion(it)
        }

    }
    private fun mostrarOpinion(view : View){
        //Recogemos la opninion
        binding.txtOpinion.text = binding.etOpinion.text
        // Ocultamos el EditText y el botón
        binding.etOpinion.visibility = View.GONE
        view.visibility = View.GONE
        // Mostramos el TextView
        binding.txtOpinion.visibility = View.VISIBLE
        // Ocultamos el teclado
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
    private fun volverEditOpinion(view: View) {
        //Hacemos visibles el edit text y el boton
        binding.etOpinion.visibility = View.VISIBLE
        binding.btHecho.visibility = View.VISIBLE
        // Hacemos invisible el propio EditText
        view.visibility = View.GONE
        binding.etOpinion.text = null
        binding.etOpinion.requestFocus()
    }
}


