package com.akinci.doggoapp.data.doggo.output

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SubBreedListResponse(
    val message: List<String>,
    val status: String
)
