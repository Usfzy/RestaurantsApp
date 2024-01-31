package com.usfzy.restaurantsapp.repository

import com.usfzy.restaurantsapp.model.Restaurant
import com.usfzy.restaurantsapp.retrofit.RestaurantsApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RestaurantDetailsRepository {

    private val restInterface = Retrofit
        .Builder()
        .baseUrl("https://restaurants-db-d98a6-default-rtdb.europe-west1.firebasedatabase.app")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(RestaurantsApiService::class.java)

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