package com.karlinux.datosalumno2

//Carlos Molines Pastor (karlinux)
// 2º DAM Semipresencial

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import com.karlinux.datosalumno2.databinding.ActivityMainBinding
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
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

        //Vamos a por el boton de guardar en historico
        binding.btnHistoricoSave.setOnClickListener() {
            introducirDatos()
        }
        //Por ultimo creamos un listener para leer los datos
        binding.btnHistoricoView.setOnClickListener() {
            leerFichero()
        }

    }

    //Creamos la funcion de lanzar la segunda actividad
    private fun lanzarSegundaActividad(view: View) {
        // Primero tenemos que ver que en el edit text haya escrito algo
        // declaramos el intent importante para despues lanzar el getResult porque se lo pasamos
        val myNombre = binding.editTxtNombre.text.toString()
        val myIntent = Intent(this, SeconActivity::class.java).apply {
            putExtra(EXTRA_NOMBRE, myNombre)
        }
        if (myNombre == "") {
            Toast.makeText(this, "No hay nombre introducido", Toast.LENGTH_SHORT).show()
        } else {
            //Lanzamos el Intent, previamente hay que realizar la constante con lo que
            // recibimos pues va todo junto
            getResult.launch(myIntent)
        }
    }

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
                binding.btnEdadGrupo.setOnClickListener() {
                    ocultarTeclado(it)
                    binding.txtResult.text = "Edad: $myEdad\n $myGrupoClase"
                    binding.btnHistoricoSave.isEnabled = true
                }

            }
            //Codigo si la segunda actividad nos devuelve un cancel
            if (it.resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Se ha cancelado", Toast.LENGTH_SHORT).show()
            }
        }

    //Ahora vamos a por GUARDAR EN HISTORICO, para ello lo primero es crear un listener en el boton
    // y luego crear la funcion que guarde en el historico, esta realmente se compone de
    // dos funciones la primera para introducir los datos y la segunda para escibirlos en el fichero
    private fun introducirDatos() {
        //Aqui aunque no sea necesario, el indicarle que no continue en caso que haya algun campo
        // vacio, lo hacems, pues podriamos tener problemas con el fichero al poder borra el nombre
        if (binding.txtFecha.text.toString() == "" || binding.txtModalidad.text.toString() == "" ||
            binding.txtCiclo.text.toString() == "" || binding.editTxtNombre.text.toString() == ""
        ) {
            Toast.makeText(this, "No hay datos que guardar", Toast.LENGTH_SHORT).show()
        } else {
            //Si no hay campos vacios, llamamos a la funcion que escribe en el fichero
            //pero antes hay que declarar variable donde se guardan los datos para escribilos
            var textoFichero: String
            textoFichero =
                binding.txtFecha.text.toString() + ";" + binding.editTxtNombre.text.toString() + ";" +
                        binding.txtModalidad.text.toString() + ";" + binding.txtCiclo.text.toString()
            escribirFichero(textoFichero)
        }
    }

    private fun escribirFichero(textoFichero: String) {
        //Ahora lo que vamos a hacer es escribir los datos en un fichero de texto
        // Le pasamos un try por si hay algun problema
        try {
            // 1º Creamos el objeto de tipo OutputStreamWriter
            val salida: OutputStreamWriter
            //Si el fichero no existe lo crea y si existe escribe en el una linea y return "\n"
            salida = OutputStreamWriter(openFileOutput(getString(R.string.filename), MODE_APPEND))
            salida.write(textoFichero + '\n')
            // Se confirma la escritura.
            salida.flush()
            salida.close()
            Toast.makeText(this, "Escritura Correcta", Toast.LENGTH_SHORT).show()
            Log.d("Archivo", "Fichero guardado correctamente")
            }
            //En el caso que de algun problema lo envia a pantalla por toast
            catch (e: IOException) {
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                //Escribimos un error al logcat
                Log.d("Archivo", "Error al escribir el fichero")
        }
    }

    //Esta funcion de mometno vemos el historico aunque despues habra que pasarlo a un view recicler
    // y para ello habra que crear un nuevo activity y un nuevo layout
    private fun leerFichero() {

        binding.txtResult.text = ""
        // Se comprueba si existe el fichero.
        if (fileList().contains(getString(R.string.filename))) {
            try {
                val entrada = InputStreamReader(openFileInput(getString(R.string.filename)))
                val br = BufferedReader(entrada)
                // Leemos la primera línea
                var linea = br.readLine()
                //Mientras que la linea no sea nula o vacia
                while (!linea.isNullOrEmpty()) {
                    // Obtenemos los datos separandolo por el ;
                    val datos: List<String> = linea.split(";")
                    val mostrar: String
                    // Montamos el texto a mostrar
                    // y lo añadimos al textView
                    mostrar = "Fecha: " + datos[0] +
                            " Nombre y apellidos: " + datos[1] +
                            " Modalidad: " + datos[2] +
                            " Ciclo: " + datos[3] + "\n"
                    binding.txtResult.append(mostrar + "\n")
                    // Leemos la siguiente línea del fichero
                    linea = br.readLine()
                }
                br.close()
                entrada.close()
            } catch (e: IOException) {
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this,
                R.string.no_existe_fichero,
                Toast.LENGTH_LONG).show()
        }
    }

    fun ocultarTeclado(view: View) {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

}


