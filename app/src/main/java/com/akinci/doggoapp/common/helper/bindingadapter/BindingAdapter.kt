package com.akinci.doggoapp.common.helper.bindingadapter

import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.util.*

@BindingAdapter("capitalizeFirst")
fun TextView.capitalizeFirst(value: String) {
    text = value.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}