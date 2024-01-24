package com.usfzy.restaurantsapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.usfzy.restaurantsapp.model.Restaurant
import com.usfzy.restaurantsapp.ui.theme.RestaurantsAppTheme
import com.usfzy.restaurantsapp.view_model.RestaurantViewModel

@Composable
fun RestaurantScreen(
    modifier: Modifier = Modifier
) {
    val viewModel: RestaurantViewModel = viewModel()

    LazyColumn(contentPadding = PaddingValues(8.dp)) {
        items(viewModel.state.value) { restaurant ->
            RestaurantItem(restaurant) { id ->
                viewModel.toggleFavorite(id)
            }
        }
    }
}

@Composable
fun RestaurantItem(
    item: Restaurant,
    onClick: (id: Int) -> Unit,
) {

    val icon = if (item.isFavorite) Icons.Filled.Favorite
    else Icons.Filled.FavoriteBorder

    Card(
        elevation = CardDefaults.cardElevation(4.dp), modifier = Modifier.padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp),
        ) {
            RestaurantIcon(
                Icons.Filled.Place, Modifier.weight(.15f)
            )
            RestaurantDetails(
                item.title, item.description, Modifier.weight(.7f)
            )
            RestaurantIcon(
                icon = icon,
                modifier = Modifier.weight(0.15f),
            ) {
                onClick(item.id)
            }
        }
    }
}

@Composable
fun RestaurantIcon(
    icon: ImageVector,
    modifier: Modifier,
    onClick: () -> Unit = {},
) {
    Image(
        imageVector = icon,
        contentDescription = null,
        modifier = modifier.clickable { onClick() },
    )
}

@Composable
fun RestaurantDetails(
    title: String, description: String, modifier: Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
        )
        CompositionLocalProvider() {
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
            )

        }

    }

}

@Preview(
    showBackground = true
)
@Composable
fun RestaurantScreenPreview() {
    RestaurantsAppTheme {
        RestaurantScreen()
    }
}