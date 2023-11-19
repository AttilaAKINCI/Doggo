package com.akinci.doggoappcompose.data.mapper

import com.akinci.doggoappcompose.data.local.entity.BreedEntity
import com.akinci.doggoappcompose.data.local.entity.ContentEntity
import com.akinci.doggoappcompose.data.local.entity.SubBreedEntity
import com.akinci.doggoappcompose.data.remote.output.BreedListResponse
import com.akinci.doggoappcompose.data.remote.output.BreedResponse
import com.akinci.doggoappcompose.data.remote.output.SubBreedListResponse
import com.akinci.doggoappcompose.ui.feaute.dashboard.data.Breed

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