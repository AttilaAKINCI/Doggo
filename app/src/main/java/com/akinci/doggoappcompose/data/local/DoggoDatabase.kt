package com.akinci.doggoappcompose.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.akinci.doggoappcompose.data.local.dao.BreedDao
import com.akinci.doggoappcompose.data.local.dao.ContentDao
import com.akinci.doggoappcompose.data.local.dao.SubBreedDao
import com.akinci.doggoappcompose.data.local.entity.BreedEntity
import com.akinci.doggoappcompose.data.local.entity.ContentEntity
import com.akinci.doggoappcompose.data.local.entity.SubBreedEntity

@Database(
    entities = [
        BreedEntity::class,
        SubBreedEntity::class,
        ContentEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class  DoggoDatabase : RoomDatabase() {
    abstract fun getBreedDao() : BreedDao
    abstract fun getSubBreedDao() : SubBreedDao
    abstract fun getContentDao() : ContentDao
}