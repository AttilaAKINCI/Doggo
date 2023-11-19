package com.akinci.doggoappcompose.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.akinci.doggoappcompose.common.room.RoomConfig

@Entity(tableName = RoomConfig.SUB_BREED_TABLE_NAME, indices = [Index(value = ["breed", "name"], unique = true)])
data class SubBreedEntity constructor(
    @PrimaryKey(autoGenerate = true)
    val id:Long = 0L,
    val breed: String,
    val name: String,
    val selected: Boolean
)