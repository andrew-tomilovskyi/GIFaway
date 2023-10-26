package com.example.gifviewer.data.network.model

import com.google.gson.annotations.SerializedName

data class GifImages(
    @SerializedName("fixed_height")
    val fixedHeight: GifImage
)