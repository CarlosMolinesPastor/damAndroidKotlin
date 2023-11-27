package com.karlinux.fragmentpractice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.karlinux.fragmentpractice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Estas dos líneas sustituyen a
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}