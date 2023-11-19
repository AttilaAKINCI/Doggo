package com.akinci.doggo.core.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.akinci.doggo.data.breed.local.BreedDao
import com.akinci.doggo.data.image.local.ImageDao
import com.akinci.doggo.data.subbreed.local.SubBreedDao
import com.akinci.doggo.data.breed.local.BreedEntity
import com.akinci.doggo.data.image.local.ImageEntity
import com.akinci.doggo.data.subbreed.local.SubBreedEntity

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

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, AppDatabaseKeys.DB_NAME)
                    .build()
                    .also { Instance = it }
            }
        }
    }
}