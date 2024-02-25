package com.akinci.doggo.data.rest

import kotlinx.serialization.Serializable

@Serializable
data class ImageServiceResponse(
    val message: List<String>
)
