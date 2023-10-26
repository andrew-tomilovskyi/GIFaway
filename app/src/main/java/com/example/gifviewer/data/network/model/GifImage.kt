package com.example.gifviewer.data.network.model

import com.google.gson.annotations.SerializedName

data class GifImage(
    @SerializedName("url")
    val url: String
)