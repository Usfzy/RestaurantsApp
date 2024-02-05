package com.usfzy.restaurantsapp.repository

import com.usfzy.restaurantsapp.model.Restaurant
import com.usfzy.restaurantsapp.retrofit.RestaurantsApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RestaurantDetailsRepository @Inject constructor(
    private val restInterface: RestaurantsApiService,
) {
    suspend fun getRestaurantDetails(id: Int): Restaurant {
        return withContext(Dispatchers.IO) {
            val response = restInterface.getRestaurant(id)

            return@withContext response.values.first().let {
                Restaurant(
                    it.id,
                    it.title, it.description,
                )
            }
        }
    }

}