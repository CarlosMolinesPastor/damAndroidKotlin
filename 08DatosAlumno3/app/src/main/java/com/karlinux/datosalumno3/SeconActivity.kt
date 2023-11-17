// ############ SECONDACTIVITY #################
package com.karlinux.datosalumno3

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.karlinux.datosalumno3.databinding.ActivitySecondBinding
import java.util.Calendar

class SeconActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySecondBinding;
    //Trabajamosc on binding previa declaracion en gradleapp
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Recogemos lo enviado desde el MainActivity, en este caso el nombre y lo mostramos
        val myNombre = intent.getStringExtra(FragmentPrincipal.EXTRA_NOMBRE)
        binding.txtNombre.text = myNombre.toString()

        //Realizamos en listener en el boton aceptar y cancelar
        binding.btnAceptar.setOnClickListener(){
            //Llamamos a la funcion mostrardatos
            mostrardatos(it)
        }
        //Se pulsa cancelar devolvemos que se ha pulsado casncelar con el LogCat y finalizamos
        // (Importante)
        binding.btnCancelar.setOnClickListener(){
            Log.d(FragmentPrincipal.TAG_APP, "Se ha pulsado Cancelar")
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
        binding.IdFechaNac.setOnClickListener(){
            //Listener que ejecuta el date picker y calcula la edad de paso
            myDatePicker()
        }
    }


    //Funcion para calcular la edad, le pasamos los dias meses y años como enteros
    private fun calcularedad (diaNac :Int, mesNac :Int, anoNac :Int) :Int{
        //declaramos las constantes
        val hoy = Calendar.getInstance() // Dia de hoy con la funcion calendar
        val mesHoy: Int
        val diaHoy: Int
        val anoHoy: Int
        diaHoy = hoy.get(Calendar.DAY_OF_MONTH)
        // ENERO - 0, FEBRERO - 1, ..., DICIEMBRE - 11
        mesHoy = hoy.get(Calendar.MONTH) + 1
        anoHoy = hoy.get(Calendar.YEAR)
        // Calculamos la edad
        var edad :Int = anoHoy - anoNac
        // Si el mes de hoy es menor al mes de nacimiento, restar un año
        if (mesHoy < mesNac || (mesHoy == mesNac && diaHoy < diaNac))
            edad--
        return edad
    }


    //Funcion para asignar grupo y clase segun lo seleccionado en los campos
    //Realizado con un array de clases y otro de grupos para poder despues si
    // hay que agregar mas datos, devuelve un string.
    private fun asignargrupoclase():String{
        //Constantes y variables , le asignamos a los radio button para despues trabajar mejor
        // con el, sin hacer uso del binding
        val rbSemi = binding.rbSemi
        val rbPres = binding.rbPresencial
        val rbAsir = binding.rbAsir
        val rbDam = binding.rbDam
        val texto : String
        var clase = 0 // clase y grupo las inicializamos
        var grupo :String = ""
        val clases : Array<Int> = arrayOf(201,202,203,204,205,206)
        val grupos : Array<String> = arrayOf("A","B","C","D","E","F")

        //Dividimos si el primer radio button esta seleccionado o el segundo
        //Asignamos los valores y los devolvemos en un string
        if (rbSemi.isChecked) {
            if (rbAsir.isChecked){
                clase = clases[0]
                grupo = grupos[0]
            } else if (rbDam.isChecked){
                clase = clases[1]
                grupo = grupos[1]
            } else {
                clase = clases[2]
                grupo = grupos[2]
            }
        }
        if (rbPres.isChecked) {
            if (rbAsir.isChecked){
                clase = clases[3]
                grupo = grupos[3]
            } else if (rbDam.isChecked){
                clase = clases[4]
                grupo = grupos[4]
            } else {
                clase = clases[5]
                grupo = grupos[5]
            }
        }
        texto = "Grupo: $grupo \n Aula: $clase"
        return texto
    }

    //Creamos las funciones para almacenar como string lo seleccionado en los Radio Buttons
    private fun modalidad():String{
        if (binding.rbSemi.isChecked)
            return "Semipresencial"
        else
            return "Presencial"
    }

    private fun ciclo():String{
        if (binding.rbAsir.isChecked)
            return "Asir"
        else if (binding.rbDam.isChecked)
            return "Dam"
        else
            return "Daw"
    }

    // Funcion mostrardatos -- Importante -- , le pasamos la vista, y comprobamos que los campos no esten
    // vacios, aqui tuve un problema con el paso de edit text a int, ya que no me dejaba
    // me daba error en el momento que lo hacia desde la variable, por ello que primero
    // lo declare como string y despues en los casos que debian de pasar los enteros hacia
    // la transformacion directamente en el llamammiento a la funcion
    private fun mostrardatos(view: View) {
        //Contstantes y variables
        //Las primeras utilizamos el split para crear constantes que nos guarden el dia mes y año
        val dia = binding.IdFechaNac.text.toString().split("/")[0]
        val mes  = binding.IdFechaNac.text.toString().split("/")[1]
        val ano = binding.IdFechaNac.text.toString().split("/")[2]

        //Declaramos la fecha para almacenarla como string
        //Declaramos Grupo clase para almacenarlo
        val fechaNac : String = binding.IdFechaNac.text.toString()
        val grupoClase : String = asignargrupoclase()
        val texto :String
        val edad :Int

        // Primero comprobamos que se haya picado en la fecha y se haya introducido una
        if ( binding.IdFechaNac.text == "Fecha Nac: ? / ? / ?" ){
            texto = "No has introducido la fecha. Pica en el cuadro azul!!"
            Toast.makeText(this, texto, Toast.LENGTH_SHORT).show()
        }
        else {
            //Calculamos la edad
            edad = calcularedad(dia.toInt(),mes.toInt(),ano.toInt())
            //Creamos el intent para pasar los datos a MainActivity
            val intentResult : Intent = Intent().apply {
                putExtra(FragmentPrincipal.EXTRA_DATE,fechaNac)
                putExtra(FragmentPrincipal.EXTRA_EDAD,edad.toString())
                putExtra(FragmentPrincipal.EXTRA_GRUPOCLASE,grupoClase)
                putExtra(FragmentPrincipal.EXTRA_CICLO, ciclo())
                putExtra(FragmentPrincipal.EXTRA_MODALIDAD,modalidad())
            }
            //Mostramos en el LogCat los datos que pasamos
            Log.d(FragmentPrincipal.TAG_APP, "Pulsado boton de aceptar")
            setResult(Activity.RESULT_OK, intentResult)
            //Finalizamos el secondActivity (Importante)
            finish()
        }
    }

    //Funcion data picker para la fecha de nacimiento, en la que comprobamos que sea correcta,
    // es decir que al menos la persona tenga un 1 dia, je je.
    // Hacemos que devuelva la edad y asi ya la tenemos para pasarla al main activity y la
    // funcion mostrardatos
    private fun myDatePicker() {
        var edad = 0
        val cal = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, day)

            // Validar la fecha seleccionada mediante la funcon fecha correcta
            if (fechacorrecta(cal)) {
                // La fecha es válida, actualiza el campo de texto
                binding.IdFechaNac.text = "${day}/${month + 1}/${year}"
            } else {
                // La fecha no es válida, muestra un mensaje de error
                Toast.makeText(this, "Fecha no válida", Toast.LENGTH_SHORT).show()
            }
        }
        DatePickerDialog(this, dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    //Funcion fechacorrecta que le pasamos un calendario y nos devuelve un booleano falso en el
    // caso que la fecha sea mayor que la actual
    private fun fechacorrecta(cal: Calendar): Boolean {
        // Aquí puedes realizar la validación de la fecha
        // Por ejemplo, verifica si la fecha seleccionada no es mayor que la fecha actual
        val correcto = true
        val currentDate = Calendar.getInstance()
        if (return cal <= currentDate)
            correcto = false
    }


}