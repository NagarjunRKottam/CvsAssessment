package com.assessment.cvs.model

import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrApiInterface {
    @GET("services/feeds/photos_public.gne?format=json&nojsoncallback=1")
    suspend fun searchImages(@Query("tags") tags: String): FlickrResponse
}