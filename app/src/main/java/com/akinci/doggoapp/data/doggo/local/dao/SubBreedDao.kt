package com.akinci.doggoapp.data.doggo.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.akinci.doggoapp.data.doggo.local.entity.SubBreedEntity

@Dao
interface SubBreedDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSubBreed(subBreedList: List<SubBreedEntity>)

    @Query("SELECT * FROM subBreedTable ORDER BY id ASC")
    fun getAllSubBreeds(): List<SubBreedEntity>

}