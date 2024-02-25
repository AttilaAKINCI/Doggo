package com.akinci.doggo.data.rest

import kotlinx.serialization.Serializable

@Serializable
data class SubBreedListServiceResponse(
    val message: List<String>
)
