package mwvdev.berightthere.android.service

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DialogServiceImpl @Inject constructor() : DialogService {

    override fun showConfirmExitDialog(
        context: Context,
        title: Int,
        message: Int?,
        positiveButtonListener: ((dialog: DialogInterface, which: Int) -> Unit)?,
        negativeButtonListener: ((dialog: DialogInterface, which: Int) -> Unit)?
    ) {
        val builder = AlertDialog.Builder(context)
            .setTitle(title)
            .setPositiveButton(android.R.string.yes, positiveButtonListener)
            .setNegativeButton(android.R.string.no, negativeButtonListener)

        message?.let { builder.setMessage(message) }

        builder.create().show()
    }

}