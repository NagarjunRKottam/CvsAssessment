package com.assessment.cvs.model

class FlickrRepository {
    private val flickrApiService = RetrofitInstance.flickrApiService

    suspend fun searchImages(tags: String): FlickrResponse {
        return flickrApiService.searchImages(tags)
    }
}