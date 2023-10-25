package com.dagoda.myficherotexto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.dagoda.myficherotexto.databinding.ActivityMainBinding
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Estas dos líneas sustituyen a setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btIntroducir.setOnClickListener{ introducirDatosFichero() }

        binding.btMostrarTodos.setOnClickListener { leerFichero() }
    }

    private fun introducirDatosFichero(){
        if(binding.etArticulo.text.isEmpty() ||  binding.etCodigo.text.isEmpty() ||
            binding.etPrecio.text.isEmpty())
            Toast.makeText(
                applicationContext, R.string.error_textos,
                Toast.LENGTH_SHORT
            ).show()
        else
        {
            var textoFichero: String

            textoFichero = binding.etArticulo.text.toString() + ";" +
                    binding.etCodigo.text.toString() + ";" +
                    binding.etPrecio.text.toString()

            escribirEnFichero(textoFichero)
        }
    }

    // Función para escribir un string en el fichero.
    private fun escribirEnFichero(datos: String) {
        try {
            val salida: OutputStreamWriter

            // Si el fichero no existe se crea,
            // si existe se añade la información.
            // Si quisieramos sobreescribir el fichero
            // utilizaríamos MODE_PRIVATE en lugar de MODE_APPEND
            salida = OutputStreamWriter(
                openFileOutput(getString(R.string.filename), MODE_APPEND)
            )

            // Se escribe en el fichero línea a línea.
            salida.write(datos + '\n')
            // Se confirma la escritura.
            salida.flush()
            salida.close()

            Toast.makeText(this, "Correcto", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }

    // Esta función va recorriendo línea a línea el fichero
    // dividiendo los datos y mostrándolos en un textView
    private fun leerFichero() {
        binding.txtContenidoFichero.text = ""
        // Se comprueba si existe el fichero.
        if (fileList().contains(getString(R.string.filename))) {
            try {
                val entrada = InputStreamReader(
                    openFileInput(getString(R.string.filename)))
                val br = BufferedReader(entrada)

                // Leemos la primera línea
                var linea = br.readLine()

                while (!linea.isNullOrEmpty()) {
                    // Obtenemos los datos separandolo por el ;
                    val datos: List<String> = linea.split(";")
                    val mostrar: String

                    // Montamos el texto a mostrar
                    // y lo añadimos al textView
                    mostrar = "Articulo: " + datos[0] + " Código: " + datos[1] + " Precio: " + datos[2]
                    binding.txtContenidoFichero.append(mostrar + "\n")

                    // Leemos la siguiente línea del fichero
                    linea = br.readLine()
                }

                br.close()
                entrada.close()
            } catch (e: IOException) {
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this, R.string.no_existe_fichero, Toast.LENGTH_LONG).show()
        }
    }
}