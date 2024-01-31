package com.usfzy.restaurantsapp.state

import com.usfzy.restaurantsapp.model.Restaurant

data class RestaurantsScreenState(
    val restaurants: List<Restaurant>,
    val isLoading: Boolean,
    val error: String? = null,
)
