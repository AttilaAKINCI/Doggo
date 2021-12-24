package com.akinci.doggoapp.common.component

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.akinci.doggoapp.R

object DialogProvider {

    fun createNoDataAlertDialog(
        context: Context,
        positiveAction: ()->Unit,
        negativeAction: (()->Unit)? = null
    ){
        with(context){
            val builder = AlertDialog.Builder(this)
                .setTitle(resources.getString(R.string.alert_title))
                .setMessage(resources.getString(R.string.alert_message))
                .setPositiveButton(resources.getString(R.string.retry)) { _, _ -> positiveAction.invoke() }

                negativeAction?.let {
                    builder.setNegativeButton(resources.getString(R.string.back)) { _, _ -> it.invoke() }
                }

                builder.show()
        }
    }
}