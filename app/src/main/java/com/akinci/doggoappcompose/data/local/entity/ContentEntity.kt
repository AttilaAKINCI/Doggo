package com.akinci.doggoappcompose.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.akinci.doggoappcompose.common.room.RoomConfig.Companion.CONTENT_TABLE_NAME

@Entity(tableName = CONTENT_TABLE_NAME, indices = [Index(value = ["contentURL"], unique = true)])
data class ContentEntity constructor(
    @PrimaryKey(autoGenerate = true)
    val id:Long = 0L,
    val breed: String,
    val subBreed: String,
    val contentURL: String
)