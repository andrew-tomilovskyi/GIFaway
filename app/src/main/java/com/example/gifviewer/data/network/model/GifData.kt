package com.example.gifviewer.data.network.model

import com.google.gson.annotations.SerializedName

data class GifData(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("images")
    val images: GifImages
)