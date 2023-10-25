package com.karlinux.datosalumno2

//Carlos Molines Pastor (karlinux)
// 2º DAM Semipresencial

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.karlinux.datosalumno2.databinding.ActivityMainBinding
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding;
    //Trabajamoscon binding previa declaracion en gradleapp
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_main)


        }
    }

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
    //Le pasamos los dias meses y años como enteros, no devuelve boolean
    private fun fechacorrecta(dia: Int, mes: Int, ano: Int) :Boolean{
        var correcto :Boolean = true

        //Hay que meter un try catch para que entienda que parsee que lo que se introduce en
        // un numero ya que si no ponemos nada se trata de las palabras day mont y year

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
    private fun asignargrupoedad():String{
        //Constantes y variables , le asignamos a los radio button para despues trabajar mejor
        // con el, sin hacer uso del binding
        val rbSemi = binding.rbSemi
        val rbPres = binding.rbPresencial
        val rbAsir = binding.rbAsir
        val rbDam = binding.rbDam
        var rbDaw = binding.rbDaw
        val texto : String
        var clase :Int = 0 // clase y grupo las inicializamos
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

    //Funcion mostrardatos, le pasamos la vista, y comprobamos que los campos no esten
    // vacios, aqui tuve un problema con el paso de edit text a int, ya que no me dejaba
    // me daba error en el momento que lo hacia desde la variable, por ello que primero
    // lo declare como string y despues en los casos que debian de pasar los enteros hacia
    // la transformacion directamente en el llamammiento a la funcion
    private fun mostrardatos(view: View) {
            //Contstantes y variables
            val dia: String = binding.txtDay.text.toString()
            val mes :String = binding.txtMonth.text.toString()
            val ano :String = binding.txtYear.text.toString()
            val texto :String
            val edad :Int

            //Comprobamos que los campos no esten vacios (primero en todas ocultamos el teclado)
            if (binding.txtIntroName.text.isEmpty()){
                ocultarTeclado(view)
                texto = "Falta Nombre y apellidos!!"
                Toast.makeText(this, texto, Toast.LENGTH_SHORT).show()
            }
            else  if ( binding.txtDay.text.isEmpty() || binding.txtMonth.text.isEmpty() ||
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
                //Si la fecha es correcta calculamos la edad y asignamos grupo y clase
                else {
                    ocultarTeclado(view)
                    edad = calcularedad(dia.toInt(),mes.toInt(),ano.toInt())
                    texto = "Edad : " + edad.toString() + "\n" + asignargrupoedad()
                    binding.txtResul.text = texto
                    binding.txtVerNombre.text = binding.txtIntroName.text
            }


    }
    //Funcion ocultarteclado, le pasamos la vista actual
    fun ocultarTeclado(view: View) {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }


}


