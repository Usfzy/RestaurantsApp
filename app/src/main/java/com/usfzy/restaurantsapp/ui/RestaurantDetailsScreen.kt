package com.usfzy.restaurantsapp.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.usfzy.restaurantsapp.view_model.RestaurantDetailsViewModel

@Composable()
fun RestaurantDetailsScreen() {
    val viewModel: RestaurantDetailsViewModel = viewModel()

    val restaurantState = viewModel.state.value

    Box {
        if (restaurantState.restaurant != null)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                RestaurantIcon(
                    icon = Icons.Filled.Place,
                    modifier = Modifier.padding(
                        top = 32.dp,
                        bottom = 32.dp,
                    )
                )

                RestaurantDetails(
                    title = restaurantState.restaurant.title,
                    description = restaurantState.restaurant.description,
                    modifier = Modifier.padding(bottom = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                )

                Text(text = "More info coming soon")
            }

        if (restaurantState.isLoading)
            CircularProgressIndicator()
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable()
fun RestaurantDetailsScreenPreview() {
    RestaurantDetailsScreen()
}