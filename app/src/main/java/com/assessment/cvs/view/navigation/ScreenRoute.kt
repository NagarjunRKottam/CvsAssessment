package com.assessment.cvs.view.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.assessment.cvs.view.DetailsScreen
import com.assessment.cvs.view.ErrorScreen
import com.assessment.cvs.view.SearchScreen
import com.assessment.cvs.viewmodel.ImageSearchViewModel

sealed class ArgumentKey(val key: String) {
    object Image : ArgumentKey("image")
    object Title : ArgumentKey("title")
    object Description : ArgumentKey("description")
    object Author : ArgumentKey("author")
    object Date : ArgumentKey("date")
}

sealed class ScreenRoute(val route: String) {
    object SearchScreen : ScreenRoute("search_screen")
    object SearchDetails :
        ScreenRoute("details_screen/{image}/{title}/{description}/{author}/{date}") {
        fun passDetails(
            image: String,
            title: String,
            description: String,
            author: String,
            date: String
        ): String {
            return "details_screen/$image/$title/$description/$author/$date"
        }
    }
}

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = ScreenRoute.SearchScreen.route
    ) {
        composable(route = ScreenRoute.SearchScreen.route) {
            val viewModel = viewModel<ImageSearchViewModel>()
            SearchScreen(navController = navController, viewModel = viewModel)
        }

        composable(
            route = ScreenRoute.SearchDetails.route,
            arguments = listOf(
                navArgument(ArgumentKey.Image.key) { type = NavType.StringType },
                navArgument(ArgumentKey.Title.key) { type = NavType.StringType },
                navArgument(ArgumentKey.Description.key) { type = NavType.StringType },
                navArgument(ArgumentKey.Author.key) { type = NavType.StringType },
                navArgument(ArgumentKey.Date.key) { type = NavType.StringType },
            )
        ) { navBackStackEntry ->
            val image = navBackStackEntry.arguments?.getString(ArgumentKey.Image.key)
            val title = navBackStackEntry.arguments?.getString(ArgumentKey.Title.key)
            val description =
                navBackStackEntry.arguments?.getString(ArgumentKey.Description.key)
            val author = navBackStackEntry.arguments?.getString(ArgumentKey.Author.key)
            val date = navBackStackEntry.arguments?.getString(ArgumentKey.Date.key)

            if (image == null || title == null || description == null || author == null || date == null) {
                return@composable ErrorScreen(navController = navController)
            }

            DetailsScreen(
                navController = navController,
                image = image,
                title = title,
                description = description,
                author = author,
                date = date
            )
        }
    }
}

