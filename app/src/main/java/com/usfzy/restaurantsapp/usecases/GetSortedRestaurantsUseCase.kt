package com.usfzy.restaurantsapp.usecases

import com.usfzy.restaurantsapp.model.Restaurant
import com.usfzy.restaurantsapp.repository.RestaurantsRepository

class GetSortedRestaurantsUseCase {
    private val repository = RestaurantsRepository()

    suspend operator fun invoke(): List<Restaurant> {
        return repository.getRestaurants().sortedBy { it.title }
    }
}