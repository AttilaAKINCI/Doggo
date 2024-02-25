package com.akinci.doggo.data.room.image

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.akinci.doggo.data.room.AppDatabaseKeys.DB_TABLE_IMAGES

@Entity(
    tableName = DB_TABLE_IMAGES,
    indices = [Index(value = ["imageUrl"], unique = true)]
)
data class ImageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val breed: String,
    val subBreed: String?,
    val imageUrl: String,
)
