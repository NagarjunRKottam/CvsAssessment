package com.assessment.cvs.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.assessment.cvs.R
import com.assessment.cvs.common.removeHtmlTags
import com.assessment.cvs.model.FlickrItem
import com.assessment.cvs.view.navigation.ScreenRoute
import com.assessment.cvs.viewmodel.ImageSearchViewModel
import java.net.URLEncoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController, viewModel: ImageSearchViewModel) {
    val searchText by viewModel.searchText.observeAsState("")
    val images by viewModel.images.observeAsState(emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { androidx.compose.material3.Text(text = "Image Search") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                )
            )
        },
        content = {
            Column(modifier = Modifier.padding(it)) {
                SearchTextField(searchText) { viewModel.onSearchTextChanged(it) }
                Spacer(modifier = Modifier.height(16.dp))
                DisplayImagesOrLoading(images, navController)
            }
        }
    )
}

@Composable
private fun SearchTextField(searchText: String, onSearchTextChange: (String) -> Unit) {
    TextField(
        value = searchText,
        onValueChange = onSearchTextChange,
        label = { Text("Search") },
        keyboardActions = KeyboardActions(
            onSearch = { onSearchTextChange(searchText) }
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun DisplayImagesOrLoading(images: List<FlickrItem>, navController: NavController) {
    if (images.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }

    } else {
        LazyColumn {
            items(images) { item ->
                ImageWithCoil(navController, item)
                Divider()
            }
        }
    }
}

@Composable
fun ImageWithCoil(navController: NavController, item: FlickrItem) {
    val painter = rememberImagePainter(
        data = item.media.m,
        builder = {
            crossfade(true)
            placeholder(R.drawable.placeholder_image)
            error(R.drawable.error_image)
        }
    )

    Column(modifier = Modifier.padding(8.dp)) {
        Text(
            text = item.title,
            style = TextStyle(fontWeight = FontWeight.Bold),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 2.dp)
        )
        Image(
            painter = painter,
            contentDescription = "image",
            modifier = Modifier
                .size(200.dp)
                .padding(bottom = 8.dp)
                .clickable {
                    navController.navigate(
                        route = ScreenRoute.SearchDetails.passDetails(
                            image = URLEncoder.encode(item.media.m, "UTF-8"),
                            title = item.title,
                            description = item.description.removeHtmlTags(),
                            author = item.author,
                            date = item.published
                        )
                    )
                }
        )
    }
}

@Preview
@Composable
fun SearchScreenPreview() {
    val navController = rememberNavController()
    val viewModel = remember { ImageSearchViewModel() } // Initialize your ViewModel here
    SearchScreen(navController = navController, viewModel = viewModel)
}