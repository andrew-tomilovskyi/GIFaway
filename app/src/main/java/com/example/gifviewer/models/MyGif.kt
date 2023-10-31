package com.example.gifviewer.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "my_gifs")
data class MyGif(
    @ColumnInfo(name = "gif_id")
    val gifId: String,
    @ColumnInfo(name = "gif_title")
    val title: String,
    @ColumnInfo(name = "gif_image")
    val image: String
) {
    @PrimaryKey
    var id: String = gifId
}