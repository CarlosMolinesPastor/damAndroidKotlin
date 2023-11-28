import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.util.Log
import android.widget.Toast

class MyDialogFragment : DialogFragment() {
    // Se crea la estructura del diálogo.
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Diálogo con DialogFragment")
            .setPositiveButton(
                android.R.string.ok
            ) { dialog, which ->
// Acciones si se pulsa SÍ.
                Log.d("DEBUG", "Acciones si SÍ.")
                Toast.makeText(requireContext(), "Acciones si SÍ",
                    Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton(
                android.R.string.cancel
            ) { dialog, which ->
// Acciones si se pulsa NO.
                Log.d("DEBUG", "Acciones si NO.")
                Toast.makeText(requireContext(), "Acciones si NO",
                    Toast.LENGTH_SHORT).show()
            }
        return builder.create()
    }
}
