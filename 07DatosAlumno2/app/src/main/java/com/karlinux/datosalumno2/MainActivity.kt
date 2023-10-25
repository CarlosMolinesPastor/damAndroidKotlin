package com.karlinux.datosalumno2

//Carlos Molines Pastor (karlinux)
// 2º DAM Semipresencial

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import com.karlinux.datosalumno2.databinding.ActivityMainBinding
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding;
    //Trabajamoscon binding previa declaracion en gradleapp

    //Creamos el Companion Object
    companion object {
        const val TAG_APP = "myDatosAlumno"

        // Lo que envia
        const val EXTRA_NOMBRE = "myNombre"

        // Los que recibe
        const val EXTRA_DATE = "myFechaNac"
        const val EXTRA_MODALIDAD = "myModalidad"
        const val EXTRA_CICLO = "myCiclo"
        const val EXTRA_GRUPOCLASE = "myGrupoClase"
        const val EXTRA_EDAD = "myEdad"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_main)

        binding.btnLeer.setOnClickListener() {
            binding.txtResult.text = ""
            lanzarSegundaActividad(it)
        }

    }

    //Creamos la funcion de lanzar la segunda actividad
    private fun lanzarSegundaActividad(view: View) {
        // Primero tenemos que ver que en el edit text haya escrito algo
        // declaramos el intent
        val myNombre = binding.editTxtNombre.text.toString()
        val myIntent = Intent(this, SeconActivity::class.java).apply {
            putExtra(EXTRA_NOMBRE, myNombre)
        }
        if (myNombre == "") {
            Toast.makeText(this, "No hay nombre introducido", Toast.LENGTH_SHORT).show()
        } else {
            //Lanzamos el Intent, previamente hay que realizar la constante con lo que
            // reciibimos pues va todo junto
            getResult.launch(myIntent)
        }
    }

    @SuppressLint("SetTextI18n")
    private val getResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            // Vamos a la Segunda Actividad a ver que devolvemos y volvemos aqui
            // Si la actividad segunda nos devuelve un OK
            if (it.resultCode == Activity.RESULT_OK) {
                val myFechaNac = it.data?.getStringExtra(EXTRA_DATE)
                val myModalidad = it.data?.getStringExtra(EXTRA_MODALIDAD)
                val myCiclo = it.data?.getStringExtra(EXTRA_CICLO)
                val myGrupoClase = it.data?.getStringExtra(EXTRA_GRUPOCLASE)
                val myEdad = it.data?.getStringExtra(EXTRA_EDAD)

                // llenamos los campos
                binding.txtFecha.text = myFechaNac
                binding.txtModalidad.text = myModalidad
                binding.txtCiclo.text = myCiclo

                //Jugamos con los botones y los focos
                //Quitamos el foco del edit text
                binding.editTxtNombre.clearFocus()
                //Habilitamos el boton de edad y grupo
                binding.btnEdadGrupo.isEnabled = true
                //Recogemos el foco en el edad grupo
                binding.btnEdadGrupo.requestFocus()
                // Añadimos un listener al boton edad grupo
                binding.btnEdadGrupo.setOnClickListener(){
                    ocultarTeclado(it)
                    binding.txtResult.text = "Edad: $myEdad\n $myGrupoClase"
                    binding.btnHistoricoSave.isEnabled = true
                }


            }
            if (it.resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Se ha cancelado", Toast.LENGTH_SHORT).show()
            }
        }




    fun ocultarTeclado(view: View) {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

}


