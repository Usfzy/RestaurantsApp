package com.usfzy.restaurantsapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
    modifier: Modifier = Modifier,
    onItemClick: (id: Int) -> Unit,
) {
    val viewModel: RestaurantViewModel = viewModel()
    val state = viewModel.state.value

    Box(
        contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(contentPadding = PaddingValues(8.dp)) {
            items(state.restaurants) { restaurant ->
                RestaurantItem(
                    restaurant,
                    onFavoriteClick = { id, oldValue ->
                        viewModel.toggleFavorite(id, oldValue)
                    },
                    onItemClick = { id ->
                        onItemClick(id)
                    },
                )
            }
        }

        if (state.isLoading) CircularProgressIndicator()
        if (state.error != null) Text(state.error)
    }
}

@Composable
fun RestaurantItem(
    item: Restaurant,
    onFavoriteClick: (id: Int, oldValue: Boolean) -> Unit,
    onItemClick: (id: Int) -> Unit,
) {

    val icon = if (item.isFavorite) Icons.Filled.Favorite
    else Icons.Filled.FavoriteBorder

    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .padding(8.dp)
            .clickable { onItemClick(item.id) },
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
                onFavoriteClick(item.id, item.isFavorite)
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
    title: String,
    description: String,
    modifier: Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start
) {
    Column(
        modifier = modifier,
        horizontalAlignment = horizontalAlignment,
    ) {
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
        RestaurantScreen {}
    }
}