package com.usfzy.restaurantsapp

import com.usfzy.restaurantsapp.model.DummyContent
import com.usfzy.restaurantsapp.model.RemoteRestaurant
import com.usfzy.restaurantsapp.retrofit.RestaurantsApiService
import kotlinx.coroutines.delay

class FakeApiService:RestaurantsApiService {
    override suspend fun getRestaurants(): List<RemoteRestaurant> {
        delay(1000)

        return DummyContent.getRemoteRestaurants()
    }

    override suspend fun getRestaurant(id: Int): Map<String, RemoteRestaurant> {
        TODO("Not yet implemented")
    }
}