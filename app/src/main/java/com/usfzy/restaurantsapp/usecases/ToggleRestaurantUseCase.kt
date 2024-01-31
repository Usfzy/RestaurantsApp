package com.usfzy.restaurantsapp.usecases

import com.usfzy.restaurantsapp.model.Restaurant
import com.usfzy.restaurantsapp.repository.RestaurantsRepository

class ToggleRestaurantUseCase {
    private val restaurantsRepository = RestaurantsRepository()
    private val getSortedRestaurantsUseCase = GetSortedRestaurantsUseCase()

    suspend operator fun invoke(id: Int, oldValue: Boolean): List<Restaurant> {
        restaurantsRepository.toggleFavoriteRestaurant(id, oldValue.not())
        return getSortedRestaurantsUseCase()
    }
}