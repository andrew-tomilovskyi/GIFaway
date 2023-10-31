package com.example.gifviewer.data.room.converters

import com.example.gifviewer.models.MyGif
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class GifConverter {

    companion object {

        @androidx.room.TypeConverter
        @JvmStatic
        fun fromGifList(list: List<MyGif>): String =
            Gson().toJson(list, object : TypeToken<List<MyGif>>() {}.type)

        @androidx.room.TypeConverter
        @JvmStatic
        fun toGifList(chapters: String): List<MyGif> =
            Gson().fromJson(chapters, object : TypeToken<List<MyGif>>() {}.type)
    }

}