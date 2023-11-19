package com.akinci.doggo.data.subbreed.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SubBreedDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSubBreeds(subBreed: List<SubBreedEntity>)

    @Query("SELECT * FROM DB_TABLE_SUB_BREED WHERE breed =:breed ORDER BY id ASC")
    suspend fun getAllSubBreeds(breed: String): List<SubBreedEntity>
}
