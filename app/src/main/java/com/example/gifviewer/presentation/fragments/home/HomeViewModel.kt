package com.example.gifviewer.presentation.fragments.home

import androidx.lifecycle.ViewModel
import com.example.gifviewer.data.network.model.GiphyResponse
import com.example.gifviewer.repositories.RepositorySearch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: RepositorySearch
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val result = searchQuery.filter { it.length > SEARCH_MIN_LENGTH }
        .flatMapLatest { query ->
            search(query)
        }

    init {
        setQuery("cat")
    }

    fun setQuery(query: String) {
        searchQuery.value = query.trim()
    }

    private fun search(query: String): Flow<Response<GiphyResponse>> = flow {
        emit(repository.searchGifs(query, API_KEY, SEARCH_LIMIT))
    }

    companion object {
        private const val API_KEY = "z65vk0jP4Ox2pcSIdra8WAefLkO49bmy"
        private const val SEARCH_LIMIT = 20
        private const val SEARCH_MIN_LENGTH = 2
    }
}