package com.akinci.doggoappcompose.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.akinci.doggoappcompose.common.room.RoomConfig.Companion.BREED_TABLE_NAME

@Entity(tableName = BREED_TABLE_NAME, indices = [Index(value = ["name"], unique = true)])
data class BreedEntity constructor(
    @PrimaryKey(autoGenerate = true)
    val id:Long = 0L,
    val name:String,
    val selected: Boolean
)