package com.example.gifviewer.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.gifviewer.data.room.converters.GifConverter
import com.example.gifviewer.data.room.dao.GifDao
import com.example.gifviewer.models.MyGif

@Database(entities = [MyGif::class], version = 2)
@TypeConverters(GifConverter::class)

abstract class Database : RoomDatabase() {

    abstract fun getGifDao() : GifDao
}