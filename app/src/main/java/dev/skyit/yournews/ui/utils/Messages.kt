package dev.skyit.yournews.ui.utils

import android.content.Context
import android.content.DialogInterface
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun View.snack(msg: String, length: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(this, msg, length)
        .show()
}

fun Context.errAlert(msg: String) {
    AlertDialog.Builder(this)
        .setTitle("Error")
        .setMessage(msg)
        .setPositiveButton("OK") { _: DialogInterface, i: Int -> }
        .show()
}

fun Fragment.snack(msg: String, length: Int = Snackbar.LENGTH_SHORT) {
    requireView().snack(msg, length)
}

fun Fragment.errAlert(msg: String) {
    requireContext().errAlert(msg)
}