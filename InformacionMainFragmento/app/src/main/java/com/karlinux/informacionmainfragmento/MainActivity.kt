package com.karlinux.informacionmainfragmento

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.karlinux.informacionmainfragmento.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var numfrag = 0
    companion object {
        const val ARG_NUMFRAG = "arg_numfrag"
        const val ARG_TEXT = "arg_text"
        const val ARG_COLORBACK = "arg_colorback"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Se muestra el primer fragment.
        showFragment()

        binding.btnChange.setOnClickListener {
        showFragment()
        }
    }

    private fun showFragment() {
        val transaction = supportFragmentManager.beginTransaction()
    // Declaración del Fragment mediante newInstance.
        val fragment = NewFragment.newInstance (++numfrag,
            binding.editTexto.text.toString(),
            (if ((numfrag % 2) == 0) Color.RED else Color.GREEN)
        )
        transaction.replace(R.id.fragment_holder,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}