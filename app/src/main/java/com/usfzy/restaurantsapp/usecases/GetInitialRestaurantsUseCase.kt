package com.usfzy.restaurantsapp.usecases

import com.usfzy.restaurantsapp.model.Restaurant
import com.usfzy.restaurantsapp.repository.RestaurantsRepository

class GetInitialRestaurantsUseCase {
    private val repository: RestaurantsRepository = RestaurantsRepository()
    private val getSortedRestaurantsUseCase = GetSortedRestaurantsUseCase()

    suspend operator fun invoke(): List<Restaurant> {
        repository.loadRestaurants()
        return getSortedRestaurantsUseCase()
    }
}