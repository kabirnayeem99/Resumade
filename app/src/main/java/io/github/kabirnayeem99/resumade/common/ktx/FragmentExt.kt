package io.github.kabirnayeem99.resumade.common.ktx

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun Fragment.showMessage(message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}


fun Fragment.showAlertDialog(
    message: String,
    positiveText: String,
    negativeText: String,
    positiveActionListener: () -> Unit,
    negativeActionListener: () -> Unit,
) {
    MaterialAlertDialogBuilder(requireContext())
        .setMessage(message)
        .setPositiveButton(positiveText) { dialog, int ->
            positiveActionListener()
            dialog.dismiss()
        }
        .setNegativeButton(negativeText) { dialog, int ->
            negativeActionListener()
            dialog.dismiss()
        }
        .create()
        .show()
}