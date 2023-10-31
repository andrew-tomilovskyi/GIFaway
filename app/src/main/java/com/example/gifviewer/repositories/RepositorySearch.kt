package com.example.gifviewer.repositories

import android.util.Log
import com.example.gifviewer.data.network.api.SearchApi
import com.example.gifviewer.models.MyGif

class RepositorySearch(private val searchApi: SearchApi) {

    suspend fun searchGifs(query: String, apiKey: String, limit: Int, offset: Int): List<MyGif>? {
        try {
            val response = searchApi.searchGifs(query, apiKey, limit, offset)

            if (response.isSuccessful) {
                return response.body()?.gifData?.map {
                    MyGif(it.id, it.title, it.images.fixedHeight.url)
                }
            } else {
                throw Exception("API request failed with ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("Giphy Error", e.message.toString())
        }

        return null
    }

}