package com.assessment.cvs.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.assessment.cvs.model.FlickrItem
import com.assessment.cvs.model.FlickrRepository
import com.assessment.cvs.model.FlickrResponse
import com.assessment.cvs.model.Media
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ImageSearchViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    private val repository: FlickrRepository = mockk()

    private lateinit var viewModel: ImageSearchViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ImageSearchViewModel().apply {
            setRepository(repository)
        }
    }

    @Test
    fun `test searchImages Success`() {
        val searchText = SEARCH_TEXT
        val mockFlickrResponse = createMockFlickrResponse()

        coEvery { repository.searchImages(searchText) } returns mockFlickrResponse

        viewModel.onSearchTextChanged(searchText)
        assertEquals(mockFlickrResponse.items, viewModel.images.value)
    }

    @Test
    fun `test searchImages Failure`() {
        val searchText = SEARCH_TEXT
        val errorMessage = FAILED_TO_FETCH_IMAGES
        coEvery { repository.searchImages(searchText) } throws Exception(errorMessage)

        viewModel.onSearchTextChanged(searchText)

        assertNull(viewModel.images.value)
    }

    private fun createMockFlickrResponse(): FlickrResponse {
        return FlickrResponse(
            title = "Recent Uploads tagged porcupine",
            link = "https://www.flickr.com/photos/tags/porcupine",
            listOf(
                FlickrItem(
                    title = "Noord-Amerikaans boomstekelvarken - Erethizon dorsatum - Canadian porcupine",
                    link = "https://www.flickr.com/photos/tom_van_deuren/53539470249/",
                    Media("https://live.staticflickr.com//65535//53539470249_251775c8ed_m.jpg"),
                    date_taken = "2024-02-17T09:04:49-08:00",
                    description = "href=\"https://www.flickr.com/people/tom_van_deuren/",
                    published = "2024-02-19T16:26:04Z",
                    author = "nobody@flickr.com (\"MrTDiddy\")",
                    author_id = "84152923@N08",
                    tags = "noordamerikaans boomstekelvarken erethizon dorsatum canadian porcupine oerzon zooantwerpen antwerpzoo antwerp zoo antwerpen"
                )
            )
        )
    }

    companion object {
        const val FAILED_TO_FETCH_IMAGES = "Failed to fetch images"
        const val SEARCH_TEXT = "porcupine"
    }
}