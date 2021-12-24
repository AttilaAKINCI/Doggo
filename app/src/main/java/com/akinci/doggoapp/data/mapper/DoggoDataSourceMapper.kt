package com.akinci.doggoapp.data.mapper

import com.akinci.doggoapp.data.local.entity.BreedEntity
import com.akinci.doggoapp.data.local.entity.ContentEntity
import com.akinci.doggoapp.data.local.entity.SubBreedEntity
import com.akinci.doggoapp.data.remote.output.BreedListResponse
import com.akinci.doggoapp.data.remote.output.BreedResponse
import com.akinci.doggoapp.data.remote.output.SubBreedListResponse
import com.akinci.doggoapp.feature.dashboard.data.Breed

fun List<Breed>.convertToBreedListEntity(): List<BreedEntity> {
    return map { BreedEntity(name = it.name, selected = it.selected) }
}

fun List<Breed>.convertToSubBreedListEntity(ownerBreed:String): List<SubBreedEntity> {
    return map { SubBreedEntity(breed = ownerBreed, name = it.name, selected = it.selected) }
}

fun List<String>.convertToDoggoContentListEntity(breed: String, subBreed: String): List<ContentEntity> {
    return map { item -> ContentEntity(breed = breed, subBreed = subBreed, contentURL = item) }
}

fun List<BreedEntity>.convertToBreedListResponse(): BreedListResponse{
    val map = mutableMapOf<String, List<Any>>()
    map { map.put(it.name, listOf()) }
    return BreedListResponse(message = map, status = "success")
}

fun List<SubBreedEntity>.convertToSubBreedListResponse(): SubBreedListResponse {
    return SubBreedListResponse(message = map { item -> item.name }, status = "success")
}

fun List<ContentEntity>.convertToBreedResponse(): BreedResponse {
    return BreedResponse(message = map { item -> item.contentURL }, status = "success")
}