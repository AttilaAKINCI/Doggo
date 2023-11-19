package com.akinci.doggoappcompose.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.akinci.doggoappcompose.data.local.entity.ContentEntity

@Dao
interface ContentDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertContent(contentList: List<ContentEntity>)

    @Query("SELECT * FROM contentTable WHERE breed=:breed AND subBreed=:subBread ORDER BY id ASC")
    fun getAllContents(breed:String, subBread:String): List<ContentEntity>

}