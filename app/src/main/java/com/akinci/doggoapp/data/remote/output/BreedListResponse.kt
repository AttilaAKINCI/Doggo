package com.akinci.doggoapp.data.remote.output

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BreedListResponse(
    val message: Map<String,List<Any>>,
    val status: String
)

