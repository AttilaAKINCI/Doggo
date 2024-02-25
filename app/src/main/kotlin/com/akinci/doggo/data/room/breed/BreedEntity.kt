package com.akinci.doggo.data.room.breed

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.akinci.doggo.data.room.AppDatabaseKeys.DB_TABLE_BREED

@Entity(
    tableName = DB_TABLE_BREED,
    indices = [Index(value = ["name"], unique = true)]
)
data class BreedEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
)
