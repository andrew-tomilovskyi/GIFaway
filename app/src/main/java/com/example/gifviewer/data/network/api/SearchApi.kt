package com.example.gifviewer.data.network.api

import com.example.gifviewer.data.network.model.GiphyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {
    @GET("search")
    suspend fun searchGifs(
        @Query("q") query: String,
        @Query("api_key") apiKey: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Response<GiphyResponse>
}