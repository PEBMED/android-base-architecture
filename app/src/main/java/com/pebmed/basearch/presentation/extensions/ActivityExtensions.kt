package com.pebmed.basearch.presentation.extensions

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.pebmed.basearch.R

fun Activity.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Activity.isValidForGlide(): Boolean = !(this.isDestroyed || this.isFinishing)

fun Context.showAlert(title: String, message: String) {
    val builder = AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle)
    builder.setTitle(title)
    builder.setMessage(message)
    builder.setCancelable(false)
    builder.setPositiveButton("ok", null)
    builder.show()
}

fun Context.showAlert(
    title: String,
    message: String,
    positiveButtonText: String,
    clickListener: DialogInterface.OnClickListener
) {
    val builder = AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle)
    builder.setTitle(title)
    builder.setMessage(message)
    builder.setCancelable(false)
    builder.setPositiveButton(positiveButtonText, clickListener)
    builder.show()
}

fun Context.showAlert(
    title: String,
    message: String,
    positiveButtonText: String,
    negativeButtonText: String,
    clickListener: DialogInterface.OnClickListener
) {
    this.showAlert(title, message, positiveButtonText, negativeButtonText, clickListener, null)
}

fun Context.showAlert(
    title: String, message: String,
    positiveButtonText: String, negativeButtonText: String,
    clickListener: DialogInterface.OnClickListener,
    cancelClickListener: DialogInterface.OnClickListener?
) {
    val builder = AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle)
    builder.setTitle(title)
    builder.setMessage(message)
    builder.setCancelable(false)
    builder.setPositiveButton(positiveButtonText, clickListener)
    builder.setNegativeButton(negativeButtonText, cancelClickListener)
    builder.show()
}

fun Context.showProgress(message: String): Dialog {
    val d = ProgressDialog(this)

    d.setMessage(message)
    d.setCancelable(false)
    d.setCanceledOnTouchOutside(false)

    d.show()
    return d
}

fun Context.showRadioButtonSelectionDialog(
    title: String,
    items: Array<String>,
    okClickListener: (index: Int) -> Unit
) {
    val builder = AlertDialog.Builder(this)
    builder.setTitle(title)
    builder.setCancelable(false)

    var checkedItem = 1
    builder.setSingleChoiceItems(items, checkedItem) { dialog, which -> checkedItem = which }

    builder.setPositiveButton("OK") { dialog, which -> okClickListener(checkedItem) }

    builder.setNegativeButton("Cancel", null)

    val dialog = builder.create()
    dialog.show()
}
