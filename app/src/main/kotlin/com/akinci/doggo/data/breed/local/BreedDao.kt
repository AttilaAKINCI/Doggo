package com.akinci.doggo.data.breed.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BreedDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBreeds(breeds: List<BreedEntity>)

    @Query("SELECT * FROM DB_TABLE_BREED ORDER BY id ASC")
    suspend fun getAllBreeds(): List<BreedEntity>
}
