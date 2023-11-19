package com.akinci.doggo.data.image.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ImageDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertImages(imageList: List<ImageEntity>)

    @Query("SELECT * FROM DB_TABLE_IMAGES WHERE breed=:breed ORDER BY id ASC")
    suspend fun getImages(breed:String): List<ImageEntity>

    @Query("SELECT * FROM DB_TABLE_IMAGES WHERE breed=:breed AND subBreed=:subBreed ORDER BY id ASC")
    suspend fun getImages(breed:String, subBreed:String): List<ImageEntity>
}
