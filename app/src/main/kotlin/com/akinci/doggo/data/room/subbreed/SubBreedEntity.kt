package com.akinci.doggo.data.room.subbreed

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.akinci.doggo.data.room.AppDatabaseKeys.DB_TABLE_SUB_BREED

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
