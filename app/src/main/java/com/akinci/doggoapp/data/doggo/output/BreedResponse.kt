package com.akinci.doggoapp.data.doggo.output

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BreedResponse(
    val message: String,
    val status: String
)

