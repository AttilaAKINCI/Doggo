package com.akinci.doggo.data.breed.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.akinci.doggo.core.storage.AppDatabaseKeys.DB_TABLE_BREED
import com.akinci.doggo.domain.data.Breed

@Entity(
    tableName = DB_TABLE_BREED,
    indices = [Index(value = ["name"], unique = true)]
)
data class BreedEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
)

private fun BreedEntity.toDomain() = Breed(name = name)

fun List<BreedEntity>.toDomain() = map { it.toDomain() }