package com.example.gifviewer.presentation.fragments

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gifviewer.models.MyGif
import com.example.gifviewer.repositories.RepositoryGifDB
import com.example.gifviewer.repositories.RepositorySearch
import com.example.gifviewer.util.downloadGifToStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GifViewModel @Inject constructor(
    private val repository: RepositorySearch,
    private val gifRepoDB: RepositoryGifDB
) : ViewModel() {

    val gifsFlow: SharedFlow<List<MyGif>?> get() = _gifsFlow
    val gifAlreadyDownloadedFlow: SharedFlow<Unit> get() = _gifAlreadyDownloadedFlow

    private val _gifsFlow = MutableSharedFlow<List<MyGif>?>()
    private val gifListDB = mutableListOf<MyGif>()
    private val _selectedGifPositionFlow = MutableStateFlow(-1)
    private val _selectedGifFlow = MutableStateFlow<MyGif?>(null)
    private val _gifAlreadyDownloadedFlow = MutableSharedFlow<Unit>()

    private val searchQuery = MutableStateFlow("")
    private val offset = MutableStateFlow(0)


    @OptIn(ExperimentalCoroutinesApi::class)
    val searchFlow = combine(searchQuery, offset) { query, currentOffset ->
        Pair(query, currentOffset)
    }
        .filter { it.first.length > SEARCH_MIN_LENGTH }
        .flatMapLatest { (query, currentOffset) ->
            search(query, currentOffset)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(10000),
            initialValue = emptyList()
        )

    val result: StateFlow<List<MyGif>> = searchFlow

    init {
        loadDefaultGifs()
    }

    fun loadDefaultGifs() {
        setQuery("cat")
    }

    fun setQuery(query: String) {
        searchQuery.value = query.trim()
        offset.value = 0 // Reset the offset when a new query is set
    }

    fun loadMore() {
        val currentOffset = offset.value
        offset.value = currentOffset + SEARCH_LIMIT
    }

    fun downloadGif(context: Context) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            val gif = _selectedGifFlow.value

            val isGifAlreadyDownloaded = gifRepoDB.getGifFromDB(gif?.id ?: "")

            if (isGifAlreadyDownloaded == null) {
                val filePath = downloadGifToStorage(
                    context = context,
                    url = gif?.image ?: "",
                    name = gif?.title ?: ""
                )
                saveGifToDB(filePath)
            } else {
                showToastGifAlreadyDownloaded()
            }
        }
    }

    fun setGifPosition(position: Int) {
        _selectedGifPositionFlow.value = position
        _selectedGifFlow.value = result.value[position]
    }

    fun getSelectedPosition(): Int {
        return _selectedGifPositionFlow.value
    }

    fun searchSavedGifs(query: String) = viewModelScope.launch {
        val filteredList = gifListDB.filter { gif ->
            gif.title.contains(query, ignoreCase = true)
        }
        _gifsFlow.emit(filteredList)
    }

    fun getMyGifsFromDB() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            val myGifs = gifRepoDB.getAllGifs() ?: emptyList()
            gifListDB.clear()
            gifListDB.addAll(myGifs)
            _gifsFlow.emit(gifListDB)
        }
    }

    fun deleteGif(position: Int) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            val gif = gifListDB[position]
            gifListDB.removeAt(position)
            _gifsFlow.emit(gifListDB)
            gifRepoDB.delete(gif)
        }
    }

    private fun search(query: String, currentOffset: Int): Flow<List<MyGif>> = flow {
        val response =
            repository.searchGifs(query, API_KEY, SEARCH_LIMIT, currentOffset) ?: emptyList()
        emit(response)
    }

    private fun saveGifToDB(filePath: String) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            gifRepoDB.insert(
                MyGif(
                    gifId = _selectedGifFlow.value?.gifId ?: "",
                    title = _selectedGifFlow.value?.title ?: "",
                    image = filePath
                )
            )
        }
    }

    private fun showToastGifAlreadyDownloaded() = viewModelScope.launch {
        _gifAlreadyDownloadedFlow.emit(Unit)
    }

    companion object {
        private const val API_KEY = "z65vk0jP4Ox2pcSIdra8WAefLkO49bmy"
        private const val SEARCH_LIMIT = 20
        private const val SEARCH_MIN_LENGTH = 2
    }
}