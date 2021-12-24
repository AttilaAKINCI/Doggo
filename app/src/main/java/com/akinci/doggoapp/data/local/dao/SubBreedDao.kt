package com.akinci.doggoapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.akinci.doggoapp.data.local.entity.SubBreedEntity

@Dao
interface SubBreedDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSubBreed(subBreedList: List<SubBreedEntity>)

    @Query("SELECT * FROM subBreedTable WHERE breed =:breed ORDER BY id ASC")
    fun getAllSubBreeds(breed: String): List<SubBreedEntity>

}