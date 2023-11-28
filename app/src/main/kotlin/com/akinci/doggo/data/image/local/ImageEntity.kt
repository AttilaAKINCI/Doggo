package com.akinci.doggo.data.image.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.akinci.doggo.core.storage.AppDatabaseKeys.DB_TABLE_IMAGES
import com.akinci.doggo.domain.data.Image

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