package com.assessment.cvs.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.assessment.cvs.model.FlickrItem
import com.assessment.cvs.model.FlickrRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class ImageSearchViewModel : ViewModel() {

    private var repository: FlickrRepository = FlickrRepository()

    private val _searchText = MutableLiveData("")
    val searchText: LiveData<String> = _searchText

    private val _images = MutableLiveData<List<FlickrItem>>()
    val images: LiveData<List<FlickrItem>> = _images

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        Log.e(TAG, "Coroutine exception ${exception.message}", exception)
    }

    fun setRepository(repo: FlickrRepository) {
        repository = repo
    }

    fun onSearchTextChanged(text: String) {
        _searchText.value = text
        searchImages(text)
    }

    private fun searchImages(tags: String) {
        viewModelScope.launch(exceptionHandler) {
            val response = repository.searchImages(tags)
            _images.value = response.items
        }
    }

    companion object {
        private const val TAG = "ImageSearchViewModel"
    }
}
