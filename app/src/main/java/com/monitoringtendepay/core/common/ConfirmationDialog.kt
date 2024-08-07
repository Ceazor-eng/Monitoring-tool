package com.monitoringtendepay.core.common

import android.app.AlertDialog
import android.content.Context

object ConfirmationDialog {

    fun show(context: Context, title: String, message: String, positiveButtonText: String, negativeButtonText: String, onPositiveClick: () -> Unit) {
        val dialogBuilder = AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButtonText) { _, _ -> onPositiveClick() }
            .setNegativeButton(negativeButtonText, null) // No action on negative button click
            .setCancelable(true)

        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }
}