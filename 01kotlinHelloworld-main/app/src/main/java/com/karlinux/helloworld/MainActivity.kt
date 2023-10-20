package com.karlinux.helloworld

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.karlinux.helloworld.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Estas dos líneas sustituyen a setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btHello.setOnClickListener { mensajeSaludo() }
    }

    private fun mensajeSaludo() {
        if (binding.etPersonName.text.toString().isEmpty()) {
            Toast.makeText(
                this, "I need your name",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                this, "Hi ${binding.etPersonName.text}!!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}