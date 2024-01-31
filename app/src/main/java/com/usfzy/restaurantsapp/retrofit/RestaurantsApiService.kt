package com.usfzy.restaurantsapp.retrofit

import com.usfzy.restaurantsapp.model.RemoteRestaurant
import com.usfzy.restaurantsapp.model.Restaurant
import retrofit2.http.GET
import retrofit2.http.Query

interface RestaurantsApiService {
    @GET("restaurants.json")
    suspend fun getRestaurants(): List<RemoteRestaurant>

    @GET("restaurants.json?orderBy=\"r_id\"")
    suspend fun getRestaurant(@Query("equalTo") id: Int): Map<String, RemoteRestaurant>
}