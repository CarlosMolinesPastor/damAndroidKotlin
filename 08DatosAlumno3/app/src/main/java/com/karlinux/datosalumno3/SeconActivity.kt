package com.karlinux.datosalumno3

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
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
    }

    //Funciones heredadas del anterior ejercicio con la salvedad que ahora  trabajamos con
    // con un txtNombre que nos viene en intent desde el MAinActivity
    //funcion para calcular el ano bisiesto y devuelve bool true or false
    private fun bisiesto(ano: Int): Boolean {
        var esBisiesto: Boolean = false
        //si es divisible por 4 y no divisble por 100, o es divisible por
        // 400 entonces es verdadero
        if ((ano % 4 == 0 && ano % 100 != 0) || (ano % 400 == 0))
            esBisiesto = true
        return esBisiesto
    }

    //Tenemos que comprobar que los dias corresponden al mes, en primer lugar
    // no pueden ser menor que 1, y por otro lado depende del mes no puede ser
    // mayor que el dia que le corresponda al mes con la salvedad de febrero que
    // si es bisiesto tendra 29 dias
    //Le pasamos los dias meses y años como enteros, nos devuelve boolean
    private fun fechacorrecta(dia: Int, mes: Int, ano: Int) :Boolean{
        var correcto :Boolean = true

        if (ano < 0)
            correcto = false
        else {
            if (mes < 1 || mes > 12)
                correcto = false
            else {
                if (dia < 1)
                    correcto = false
                else {
                    if ((mes == 1 || mes == 3 || mes == 5 || mes == 7 || mes == 8 || mes == 10 || mes == 12) && dia > 31)
                        correcto = false
                    if (mes == 2 && bisiesto(ano) && dia > 29)
                    //Llamamos a la funcion bisiesto y le pasaos el ano
                        correcto = false
                    if (mes == 2 && !bisiesto(ano) && dia > 28)
                        correcto = false
                    if ((mes == 4 || mes == 6 || mes == 9 || mes == 11) && dia > 30)
                        correcto = false
                }
            }
        }

        return correcto
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

        var edad :Int = anoHoy - anoNac

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
        val dia :String = binding.txtDay.text.toString()
        val mes :String = binding.txtMonth.text.toString()
        val ano :String = binding.txtYear.text.toString()
        //Declaramos la fecha para almacenarla como string
        val fechaNac :String = "$dia/$mes/$ano"
        //Declaramos Grupo clase para almacenarlo
        val grupoClase : String = asignargrupoclase()
        val texto :String
        val edad :Int

        // Primero comprobamos que los campos no esten vacios (primero en todas ocultamos el teclado)
        if ( binding.txtDay.text.isEmpty() || binding.txtMonth.text.isEmpty() ||
            binding.txtYear.text.isEmpty() ){
            ocultarTeclado(view)
            texto = "Falta algun dato de la fecha!!"
            Toast.makeText(this, texto, Toast.LENGTH_SHORT).show()
        }
        //Si los campos no estan vacios comprobamos que la fecha sea correcta
        else if (!fechacorrecta(dia.toInt(),mes.toInt(),ano.toInt())){
            ocultarTeclado(view)
            texto = "Fecha Incorrecta!!"
            Toast.makeText(this, texto, Toast.LENGTH_SHORT).show()
        }
        //Si la fecha es correcta y todo OK pasamos a MainActivity
        else {
            ocultarTeclado(view)
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


    //Funcion ocultarteclado, le pasamos la vista actual
    fun ocultarTeclado(view: View) {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

}