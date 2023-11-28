package com.karlinux.myalert

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.graphics.Color
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.karlinux.myalert.databinding.ActivityMainBinding
import com.karlinux.myalert.databinding.DialogLayoutBinding
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            // Obtenemos un array con los nombres de las asignaturas para usarlo en Listas y Seleccion Simple
            val asignaturasArray: Array<String> =
                resources.getStringArray(R.array.array_asignaturas)

            btnAlertDialog.setOnClickListener {
                myAlertDialog(
                    "Este es el primer cuadro de diálogo, " +
                            "se utiliza la clase AlertDialog para mostrarlo."
                )
            }

            btnalerDialogList.setOnClickListener {
                myAlertDialogList()
            }

            btnalerDialogSeleccionSimple.setOnClickListener {
                myAlertDialogSeleccionSimple(asignaturasArray)
            }

            btnalerDialogListaSeleccionMultiple.setOnClickListener {
                myAlertDialogListaSeleccionMultiple(asignaturasArray)
            }

            btnAlerDialogCustom.setOnClickListener {
                myAlertDialogCustom()
            }
            // ############################### AlertDialog con DialogFragment.
            btnAlerDialogFragment.setOnClickListener {
                val myDialogFragment = MyDialogFragment()
                myDialogFragment.show(supportFragmentManager, "teGusta")
            }

            btnTimePicker.setOnClickListener {
                myTimePicker()
            }
            btnDatePicker.setOnClickListener {
                myDatePicker()
            }
        }
    }

    // ############################### AlertDialog simple.
    private fun myAlertDialog(message: String) {
        val builder = AlertDialog.Builder(this)
        // Se crea el AlertDialog.
        builder.apply {
            // Se asigna un título.
            setTitle("My AlertDialog!!")
            // Se asgina el cuerpo del mensaje.
            setMessage(message)
            // Se define el comportamiento de los botones.
            setPositiveButton(
                android.R.string.ok,
                DialogInterface.OnClickListener(function = actionButton)
            )
            setNegativeButton(android.R.string.cancel) { _, _ ->
                Toast.makeText(
                    context, android.R.string.no,
                    Toast.LENGTH_SHORT
                ).show()
                binding.root.setBackgroundColor(Color.RED)
            }
            setNeutralButton("No sé") { _, _ ->
                Toast.makeText(context, "No sé", Toast.LENGTH_SHORT).show()
                binding.root.setBackgroundColor(Color.WHITE)
            }
        }
        // Se muestra el AlertDialog.
        builder.show()
    }

    private val actionButton = { dialog: DialogInterface, which: Int ->
        Toast.makeText(this, android.R.string.ok, Toast.LENGTH_SHORT).show()
        binding.root.setBackgroundColor(Color.GREEN)
    }

    // ############################### AlertDialog con lista de opciones.
    private fun myAlertDialogList() {
        val builder = AlertDialog.Builder(this)
        // Obtenemos un array con los nombres de las asignaturas
        val asignaturasArray = resources.getStringArray(R.array.array_asignaturas)
        builder.apply {
            setTitle("Lista de Asignaturas")
            setItems(asignaturasArray) { _, which ->
                Toast.makeText(
                    context, asignaturasArray[which],
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        builder.show()
    }

    // ############################### AlertDialog con seleccion simple. RadioButtons
    private fun myAlertDialogSeleccionSimple(asignaturas: Array<String>) {
        val builder = AlertDialog.Builder(this)
        builder.apply {
            setTitle("My AlertDialog con lista simple")
            setSingleChoiceItems(asignaturas, -1) { _, which ->
                Log.d("DEBUG", asignaturas[which])
            }
            setPositiveButton(android.R.string.ok) { dialog, _ ->
                val selectedPosition = (dialog as AlertDialog)
                    .listView.checkedItemPosition
                Toast.makeText(
                    context, asignaturas[selectedPosition],
                    Toast.LENGTH_SHORT
                ).show()
            }
            setNegativeButton(android.R.string.cancel) { _, _ ->
                Toast.makeText(
                    context, android.R.string.no,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        builder.show()
    }

    // ############################### AlertDialog con seleccion multiple. CheckBoxes
    private fun myAlertDialogListaSeleccionMultiple(asignaturas: Array<String>) {
        val builder = AlertDialog.Builder(this)
        // Lista de enteros donde iremos metiendo los items seleccionados
        val selectedItems = ArrayList<Int>()
        builder.apply {
            setTitle("My AlertDialog con lista multiple")
            setMultiChoiceItems(asignaturas, null) { _, which, isChecked ->
                if (isChecked) {
                    selectedItems.add(which)
                    Log.d("DEBUG", "Checked: " + asignaturas[which])
                } else if (selectedItems.contains(which)) {
                    selectedItems.remove(which)
                    Log.d("DEBUG", "UnChecked: " + asignaturas[which])
                }
            }
            setPositiveButton(android.R.string.ok) { _, _ ->
                var textToShow = "Checked: "
                if (selectedItems.size > 0) {
                    for (item in selectedItems) {
                        textToShow = textToShow + asignaturas[item] + " "
                    }
                } else textToShow = "No items checked!"
                Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show()
            }
            setNegativeButton(android.R.string.cancel) { _, _ ->
                Toast.makeText(
                    context, android.R.string.cancel,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        builder.show()
    }

    // ############################### AlertDialog con layout personalizado.
    private fun myAlertDialogCustom() {
        //Hay que crear un layout personalizado para el AlertDialog
        val builder = AlertDialog.Builder(this)
        builder.apply {
            // Así es con View Binding
            var bindingDialog: DialogLayoutBinding
            // Inflamos y asignamos el layout
            bindingDialog = DialogLayoutBinding.inflate(layoutInflater)
            setView(bindingDialog.root)
            setPositiveButton(android.R.string.ok) { dialog, _ ->
                // Haciendo uso de View Binding
                val name = bindingDialog.username.text
                val pass = bindingDialog.password.text
                Toast.makeText(
                    context, "User: $name\nPass: $pass",
                    Toast.LENGTH_SHORT
                ).show()
            }
            setNegativeButton(android.R.string.cancel) { dialog, _ ->
                Toast.makeText(
                    context, android.R.string.cancel,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        builder.show()
    }

    private fun myTimePicker() {
        // Creamos una instancia de tipo calendario.
        val cal = Calendar.getInstance()
        // Creamos el OnTimeSetListener, que se pasará luego al diálog
        // y se ejecutará al aceptar
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            // Asignamos a la instancia del calendario la hora elegida
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
        }
        binding.tvTimeDatePicker.text = SimpleDateFormat("HH:mm").format(cal.time)
        // Llamada al TimePickerDialog, pasando el listener y la hora actual
        TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun myDatePicker() {
        val cal = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year,
                                                                   month, day ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, day)
            binding.tvTimeDatePicker.text = "${cal.get(Calendar.DAY_OF_MONTH)}" +
                    "/${cal.get(Calendar.MONTH) + 1}" +
                    "/${cal.get(Calendar.YEAR)}"
        }
        DatePickerDialog(
            this,
            dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

}




