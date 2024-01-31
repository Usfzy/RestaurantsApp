package com.usfzy.restaurantsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.usfzy.restaurantsapp.ui.RestaurantDetailsScreen
import com.usfzy.restaurantsapp.ui.RestaurantScreen
import com.usfzy.restaurantsapp.ui.theme.RestaurantsAppTheme
import com.usfzy.restaurantsapp.view_model.RestaurantViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RestaurantsAppTheme {
                RestaurantsApp()
            }
        }
    }
}

@Composable
private fun RestaurantsApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "restaurants") {
        composable(route = "restaurants") {
            val viewModel: RestaurantViewModel = viewModel()

            RestaurantScreen(
                state = viewModel.state.value,
                onItemClick = {
                    navController.navigate(
                        "restaurants/${it}"
                    )
                },
                onFavoriteClick = { id, oldValue ->
                    viewModel.toggleFavorite(id, oldValue)
                }
            )
        }

        composable(
            route = "restaurants/{restaurant_id}",
            arguments = listOf(navArgument("restaurant_id") {
                type = NavType.IntType
            }),
        ) {
            val id = it.arguments?.getInt("restaurant_id")
            RestaurantDetailsScreen()
        }
    }


}