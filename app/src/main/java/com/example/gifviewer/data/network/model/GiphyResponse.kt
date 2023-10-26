package com.example.gifviewer.data.network.model

import com.google.gson.annotations.SerializedName

data class GiphyResponse(
    @SerializedName("data")
    val gifData: List<GifData>
)