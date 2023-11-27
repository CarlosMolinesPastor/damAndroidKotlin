package com.karlinux.fragmentpractice

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.karlinux.fragmentpractice.databinding.FragmentButtonsBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ButtonsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ButtonsFragment : Fragment() {
    private lateinit var binding: FragmentButtonsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
// Inflate the layout for this fragment
        binding = FragmentButtonsBinding.inflate(inflater)
        binding.button.setOnClickListener { actionsButtons("button") }
        binding.imageButton.setOnClickListener { actionsButtons("imageButton") }
        binding.fabButton.setOnClickListener { fabActionButton() }

        return binding.root
    }

    private fun actionsButtons(name: String) {
        when (name) {
            "button" -> {
                binding.checkBox.isChecked = true
                binding.radioButton1.isChecked = true
                binding.radioButton2.isChecked = false
                binding.toggleButton.isChecked = true
                binding.switch1.isChecked = true
            }
            else -> {
                binding.checkBox.isChecked = false
                binding.radioButton1.isChecked = false
                binding.radioButton2.isChecked = true
                binding.toggleButton.isChecked = false
                binding.switch1.isChecked = false
            }
        }
    }
    private fun fabActionButton() {
        var texto: String
        texto = "Pulsados: \n";
        if (binding.checkBox.isChecked)
            texto += "CheckBox "
        if (binding.radioButton1.isChecked)
            texto += "Radio 1 "
        else {
            if (binding.toggleButton.isChecked)
                texto += "Toggle Button "
            if (binding.switch1.isChecked)
                texto += "Switch"
        }
        Toast.makeText(activity, texto, Toast.LENGTH_LONG).show()
    }

}