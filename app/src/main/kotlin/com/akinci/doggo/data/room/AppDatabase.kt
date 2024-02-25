package com.akinci.doggo.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.akinci.doggo.data.room.breed.BreedDao
import com.akinci.doggo.data.room.breed.BreedEntity
import com.akinci.doggo.data.room.image.ImageDao
import com.akinci.doggo.data.room.image.ImageEntity
import com.akinci.doggo.data.room.subbreed.SubBreedDao
import com.akinci.doggo.data.room.subbreed.SubBreedEntity

@Database(
    entities = [
        BreedEntity::class,
        SubBreedEntity::class,
        ImageEntity::class
    ],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getBreedDao(): BreedDao
    abstract fun getSubBreedDao(): SubBreedDao
    abstract fun getImageDao(): ImageDao
}