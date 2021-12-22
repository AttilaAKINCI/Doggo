package com.akinci.doggoapp.data.doggo.output

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BreedListResponse(
    val message: Map<String,List<Any>>,
    val status: String
)

