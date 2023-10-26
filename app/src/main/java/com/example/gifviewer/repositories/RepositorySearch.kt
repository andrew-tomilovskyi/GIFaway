package com.example.gifviewer.repositories

import com.example.gifviewer.data.network.api.SearchApi

class RepositorySearch(private val searchApi: SearchApi) {
    suspend fun searchGifs(query: String, apiKey: String, limit: Int) =
        searchApi.searchGifs(query, apiKey, limit)
}