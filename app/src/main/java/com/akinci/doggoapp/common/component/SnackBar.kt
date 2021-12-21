package com.akinci.doggoapp.common.component

import android.view.View
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber

class SnackBar {
    companion object{
        fun make(view : View, text: CharSequence) : Snackbar {
            // log every snackBar messages
            Timber.d(text.toString())
            return Snackbar.make(view, text, Snackbar.LENGTH_SHORT)
        }
    }
}