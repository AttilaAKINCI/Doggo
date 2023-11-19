package com.akinci.doggoappcompose.common.helper

import java.util.*

fun String.capitalize(): String{
    return replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}

fun String.allCaps(): String{
    return uppercase(Locale.getDefault())
}

