package com.karlinux.datosalumno2

//Carlos Molines Pastor (karlinux)
// 2º DAM Semipresencial

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.karlinux.datosalumno2.databinding.ActivityMainBinding
import java.io.IOException
import java.io.OutputStreamWriter

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

        binding.btnHistoricoSave.isEnabled = false


        //Listener al boton de leer para lanzar la segunda actividad, vaciando el txt Result
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
            lanzarTerceraActividad(it)
        }

    }

    //Creamos la funcion de lanzar la segunda actividad
    private fun lanzarSegundaActividad(view: View) {
        // Primero tenemos que ver que en el edit text haya escrito algo
        // declaramos el intent importante para despues lanzar el getResult porque se lo pasamos
        // como parametro
        //Constante nombre
        val myNombre = binding.editTxtNombre.text.toString()
        //Crwamos el intent y le pasamos el nombre
        val myIntent = Intent(this, SeconActivity::class.java).apply {
            putExtra(EXTRA_NOMBRE, myNombre)
        }
        //Si el nombre esta vacio, mostramos un toast, si no lanzamos el intent con el getResult
        if (myNombre == "") {
            Toast.makeText(this, "No hay nombre introducido", Toast.LENGTH_SHORT).show()
        } else {
            //Lanzamos el Intent, previamente hay que realizar la constante con lo que
            // recibimos pues va todo junto
            getResult.launch(myIntent)
        }
    }

    //Declaramos el getResult, que es el que recoge los datos de la segunda actividad
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

                binding.btnHistoricoSave.isEnabled = false

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
                // Añadimos un listener al boton edad grupo, ocultamos el teclado y mostramos
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
    // dos funciones la primera para introducir los datos (que es la que llamamos desde el boton
    // y la segunda para escibirlos en el fichero
    private fun introducirDatos() {
        //Aqui aunque no sea necesario, el indicarle que no continue en caso que haya algun campo
        // vacio, lo hacems, pues podriamos tener problemas con el fichero al poder borrar el nombre
        if (binding.txtFecha.text.toString() == "" || binding.txtModalidad.text.toString() == "" ||
            binding.txtCiclo.text.toString() == "" || binding.editTxtNombre.text.toString() == ""
        ) {
            Toast.makeText(this, "No hay datos que guardar", Toast.LENGTH_SHORT).show()
        } else {

            // Primero para pasar los datos de la fecha por separado dia, mes y año, cortando el string por "/"
            // Y guardandolo en variables dia mes y año
            val datosFecha :List<String> = binding.txtFecha.text.toString().split("/")
            val dia = datosFecha[0]
            // El mes le pasamos la funcion asignarMes que nos devuelve el nombre del mes
            val mes = asignarMes(datosFecha[1].toInt())
            val ano = datosFecha[2]

            //Ahora vamos a por el grupo clase, que lo vamos a coger del txtResult, y recogemos lo
            // dividimos por el salto de linea y cogemos las dos ultimas lineas y las guardamos en
            // una variable
            val result = binding.txtResult.text.toString().split("\n")
            val grupoClase = result[1] + result[2]
            //Por ultimo creamos una variable que nos guarde el texto que vamos a escribir en el fichero
            val textoFichero: String
            textoFichero = dia + ";" + mes + ";" + ano + ";" + binding.editTxtNombre.text.toString() + ";" +
                        binding.txtModalidad.text.toString() + ";" +
                        binding.txtCiclo.text.toString()+ ";" + grupoClase.toString()
            //Llamamos a la funcion escribir fichero y le pasamos el texto que hemos creado
            escribirFichero(textoFichero)
        }
    }

    //Funcion para escribir en el fichero
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
            //Mostramos un toast y escribimos en el logcat
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

    //Funcion para lanzar la Tercera Actividad creando un intent y pasandoselo a la ThirdActivity
    private fun lanzarTerceraActividad(view: View) {
        val myIntent2 = Intent(this, ThirdActivity::class.java).apply {
        }
        startActivity(myIntent2)
    }

    //Funcion para asignar el nombre del mes al mes mediante un when (switch
    private fun asignarMes(mes: Int): String {
        var mesString: String = ""
        when (mes) {
            1 -> mesString = "Enero"
            2 -> mesString = "Febrero"
            3 -> mesString = "Marzo"
            4 -> mesString = "Abril"
            5 -> mesString = "Mayo"
            6 -> mesString = "Junio"
            7 -> mesString = "Julio"
            8 -> mesString = "Agosto"
            9 -> mesString = "Septiembre"
            10 -> mesString = "Octubre"
            11 -> mesString = "Noviembre"
            12 -> mesString = "Diciembre"
        }
        return mesString
    }

    //Funcion para ocultar el teclado
    fun ocultarTeclado(view: View) {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

}


