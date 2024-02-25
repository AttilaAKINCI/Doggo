package com.akinci.doggo.domain.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Chip(
    val name: String,
    val selected: Boolean = false,
) : Parcelable
