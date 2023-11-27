package com.karlinux.informacionmainfragmento

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.karlinux.informacionmainfragmento.MainActivity.Companion.ARG_COLORBACK
import com.karlinux.informacionmainfragmento.MainActivity.Companion.ARG_NUMFRAG
import com.karlinux.informacionmainfragmento.MainActivity.Companion.ARG_TEXT
import com.karlinux.informacionmainfragmento.databinding.FragmentNewBinding

class NewFragment : Fragment() {
    private lateinit var binding: FragmentNewBinding
    private var numFrag: Int? = null
    private var textoActivity: String? = null
    private var colorBack: Int? = null
    private val TAG = "FragmentNew - ${numFrag}"

        override fun onAttach(context: Context) {
        Log.d(TAG, "onAttach")
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)

        // Si existen argumentos pasados en el Bundle desde la llamada,
        // se asignan a las propiedades.
        // ARG_NUMFRAG, ARG_TEXT y ARG_COLORBACK están declaradas en MainActivity.
        arguments?.let {
            numFrag = it.getInt(ARG_NUMFRAG)
            textoActivity = it.getString(ARG_TEXT)
            colorBack = it.getInt(ARG_COLORBACK)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView")
        binding = FragmentNewBinding.inflate(inflater)
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param nFrag Número de fragment.
         * @param tActivity Texto de la actividad.
         * @param cBack Color de fondo.
         * @return A new instance of fragment NewFragment.
         */

        @JvmStatic
        fun newInstance(nFrag: Int, tActivity: String, cBack: Int) =
            NewFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_NUMFRAG, nFrag)
                    putString(ARG_TEXT, tActivity)
                    putInt(ARG_COLORBACK, cBack)
                }
            }
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated")
        // Mostramos en los componentes los datos pasados
        binding.frameRoot.setBackgroundColor(colorBack!!)
        binding.tvNumfragment.text = "Fragment\n${numFrag}"
        binding.tvTexto.text = textoActivity
        super.onViewCreated(view, savedInstanceState)
    }
}
