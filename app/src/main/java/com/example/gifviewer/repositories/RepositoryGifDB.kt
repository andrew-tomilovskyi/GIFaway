package com.example.gifviewer.repositories

import com.example.gifviewer.data.room.dao.GifDao
import com.example.gifviewer.models.MyGif
import javax.inject.Inject

class RepositoryGifDB @Inject constructor(private val gifDao: GifDao) {

    suspend fun insert(gif: MyGif) = gifDao.insert(gif)

    suspend fun getAllGifs() = gifDao.getAllGifs()

    suspend fun getGifFromDB(id: String) = gifDao.getGifFromDB(id)

    suspend fun delete(gif: MyGif) = gifDao.delete(gif)
}