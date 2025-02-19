package com.ad.mvvmstarter.utility.dialog

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import com.ad.mvvmstarter.R


object DialogUtils {
    fun showAlertDialog(
        context: Context,
        title: String,
        message: String,
        isCancelable: Boolean = false,
        onPositiveCallback: ((DialogInterface?) -> Unit)? = null,
        positiveBtnText: String = context.getString(R.string.ok)
    ) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setMessage(message)
        builder.setTitle(title)
        builder.setCancelable(isCancelable)
        builder.setPositiveButton(
            positiveBtnText
        ) { dialog: DialogInterface?, _: Int ->
            onPositiveCallback?.invoke(dialog)
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    fun showConfirmationDialog(
        context: Context,
        title: String,
        message: String = "",
        isCancelable: Boolean = false,
        onPositiveCallback: ((DialogInterface?) -> Unit)? = null,
        onNegativeCallback: ((DialogInterface?) -> Unit)? = null,
        positiveBtnText: String = context.getString(R.string.yes),
        negativeBtnText: String = context.getString(R.string.no)
    ) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setMessage(message)
        builder.setTitle(title)
        builder.setCancelable(isCancelable)
        builder.setPositiveButton(
            positiveBtnText
        ) { dialog: DialogInterface?, _: Int ->
            onPositiveCallback?.invoke(dialog)
        }
        builder.setNegativeButton(
            negativeBtnText
        ) { dialog: DialogInterface, _: Int ->
            dialog.cancel()
            onNegativeCallback?.invoke(dialog)
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }
}