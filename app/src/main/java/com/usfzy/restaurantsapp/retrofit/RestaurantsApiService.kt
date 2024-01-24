package com.usfzy.restaurantsapp.retrofit

import com.usfzy.restaurantsapp.model.Restaurant
import retrofit2.http.GET

interface RestaurantsApiService {
    @GET("restaurants.json")
    suspend fun getRestaurants(): List<Restaurant>
}