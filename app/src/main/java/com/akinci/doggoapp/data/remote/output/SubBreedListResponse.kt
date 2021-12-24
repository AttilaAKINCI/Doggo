package com.akinci.doggoapp.data.remote.output

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SubBreedListResponse(
    val message: List<String>,
    val status: String
)
