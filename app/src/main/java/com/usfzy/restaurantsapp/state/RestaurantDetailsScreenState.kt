package com.usfzy.restaurantsapp.state

import com.usfzy.restaurantsapp.model.Restaurant

data class RestaurantDetailsScreenState(
    val restaurant: Restaurant? = null,
    val isLoading: Boolean,
    val error: String? = null,
)
