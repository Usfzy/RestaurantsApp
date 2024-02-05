package com.usfzy.restaurantsapp.usecases

import com.usfzy.restaurantsapp.model.Restaurant
import com.usfzy.restaurantsapp.repository.RestaurantsRepository
import javax.inject.Inject

class GetSortedRestaurantsUseCase @Inject constructor(
    private val repository: RestaurantsRepository
) {
    suspend operator fun invoke(): List<Restaurant> {
        return repository.getRestaurants().sortedBy { it.title }
    }
}