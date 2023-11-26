package com.akinci.doggo.core.utils

import java.util.Locale

fun String.capitalise() = replaceFirstChar { char ->
    if (char.isLowerCase()) {
        char.titlecase(Locale.getDefault())
    } else {
        char.toString()
    }
}