package com.akinci.doggo.data.subbreed.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.akinci.doggo.core.storage.AppDatabaseKeys.DB_TABLE_SUB_BREED
import com.akinci.doggo.domain.subBreed.SubBreed

@Entity(
    tableName = DB_TABLE_SUB_BREED,
    indices = [Index(value = ["breed", "name"], unique = true)]
)
data class SubBreedEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val breed: String,
    val name: String,
)

private fun SubBreedEntity.toDomain() = SubBreed(
    breed = breed,
    name = name
)

fun List<SubBreedEntity>.toDomain() = map { it.toDomain() }