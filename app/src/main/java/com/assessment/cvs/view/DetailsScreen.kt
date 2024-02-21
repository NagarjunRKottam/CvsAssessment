package com.assessment.cvs.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.assessment.cvs.R
import com.assessment.cvs.view.navigation.ScreenRoute
import java.net.URLDecoder


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    navController: NavController,
    image: String,
    title: String,
    description: String,
    author: String,
    date: String
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Image Details") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(route = ScreenRoute.SearchScreen.route) {
                            popUpTo(ScreenRoute.SearchScreen.route) {
                                inclusive = true
                            }
                        }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = {
            Column(
                modifier = Modifier.padding(it)
            ) {
                TextRow("TITLE:", title)
                ImageWithCoil(URLDecoder.decode(image, "UTF-8"), contentDescription = "Image")
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = description,
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(bottom = 16.dp),
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 24.sp
                    )
                )
                TextRow("AUTHOR:", author)
                TextRow("PUBLISHED:", date)
            }
        }
    )
}

@Composable
fun TextRow(
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Text(
            text = label,
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = value,
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun ImageWithCoil(
    item: String,
    contentDescription: String
) {
    val painter = rememberImagePainter(
        data = item,
        builder = {
            crossfade(true)
            placeholder(R.drawable.placeholder_image)
            error(R.drawable.error_image)
        }
    )
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Image(
            painter = painter,
            contentDescription = contentDescription,
            modifier = Modifier
                .size(200.dp)
        )
    }
}

@Preview
@Composable
fun DetailsScreenPreview() {
    val navController = rememberNavController()
    val image = "YourImageURL" // Provide a sample image URL
    val title = "Sample Title"
    val description = "Sample Description"
    val author = "Sample Author"
    val date = "Sample Date"
    DetailsScreen(
        navController = navController,
        image = image,
        title = title,
        description = description,
        author = author,
        date = date
    )
}




