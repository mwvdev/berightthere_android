package mwvdev.berightthere.android.service

import android.content.Context
import android.content.DialogInterface

interface DialogService {

    fun showConfirmExitDialog(
        context: Context,
        title: Int,
        message: Int?,
        positiveButtonListener: ((dialog: DialogInterface, which: Int) -> Unit)? = null,
        negativeButtonListener: ((dialog: DialogInterface, which: Int) -> Unit)? = null
    )

}