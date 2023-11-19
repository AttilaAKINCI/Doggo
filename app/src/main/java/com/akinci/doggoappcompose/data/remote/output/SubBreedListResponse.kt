package com.akinci.doggoappcompose.data.remote.output

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SubBreedListResponse(
    val message: List<String>,
    val status: String
)
