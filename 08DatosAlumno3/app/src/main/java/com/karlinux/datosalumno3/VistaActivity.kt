// ######### VISTA DE LOS DATOS DEL ALUMNO #########
package com.karlinux.datosalumno3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.karlinux.datosalumno3.databinding.ActivityVistaBinding

class VistaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVistaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVistaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Recogemos los datos enviados desde el adapter que se encuentra en DatosAdapter
        val mynombre = intent.getStringExtra(DatosAdapter.EXTRA_NOMBRE)
        val myGrupoClase = intent.getStringExtra(DatosAdapter.EXTRA_GRUPOCLASE)
        val myModalidad = intent.getStringExtra(DatosAdapter.EXTRA_MODALIDAD)
        val myCiclo = intent.getStringExtra(DatosAdapter.EXTRA_CICLO)
        val myDia = intent.getStringExtra(DatosAdapter.EXTRA_DIA)
        val myMes = intent.getStringExtra(DatosAdapter.EXTRA_MES)
        val myAno = intent.getStringExtra(DatosAdapter.EXTRA_ANO)

        //Separarmos el grupo y el aula
        val partes = myGrupoClase.toString().split(" ")
        val grupo = partes[2] //Esto es el grupo
        val aula = partes[5]  // Esto es el aula

        //Mostramos los datos en los TextView
        binding.txtNombre.text = mynombre.toString()
        binding.txtGrupoV.text = "Grupo: " + grupo
        binding.txtAulaV.text = "Aula: " + aula
        binding.txtCicloV.text = myCiclo.toString() + " "
        binding.txtModalidadV.text = myModalidad.toString()
        binding.txtFechaV.text = myDia.toString() + " de " + myMes.toString() + " de " + myAno.toString()
    }
}