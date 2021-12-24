package com.akinci.doggoapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.akinci.doggoapp.data.local.entity.ContentEntity

@Dao
interface ContentDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertContent(contentList: List<ContentEntity>)

    @Query("SELECT * FROM contentTable ORDER BY id ASC")
    fun getAllContents(): List<ContentEntity>

}