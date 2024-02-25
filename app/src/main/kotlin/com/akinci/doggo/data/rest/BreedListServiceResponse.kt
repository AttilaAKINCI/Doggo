package com.akinci.doggo.data.rest

import kotlinx.serialization.Serializable

@Serializable
data class BreedListServiceResponse(
    val message: Map<String, List<String>>,
)
