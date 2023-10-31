package com.example.gifviewer.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gifviewer.models.MyGif

@Dao
interface GifDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(gif: MyGif)

    @Query("SELECT * FROM my_gifs")
    suspend fun getAllGifs() : List<MyGif>?

    @Query("SELECT * FROM my_gifs WHERE gif_id = :id")
    suspend fun getGifFromDB(id: String) : MyGif

    @Delete
    suspend fun delete(gif: MyGif)

}