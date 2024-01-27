package com.usfzy.restaurantsapp.view_model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.usfzy.restaurantsapp.model.Restaurant
import com.usfzy.restaurantsapp.retrofit.RestaurantsApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RestaurantDetailsViewModel(stateHandle: SavedStateHandle) : ViewModel() {
    private val restInterface: RestaurantsApiService

    val state: MutableState<Restaurant?> = mutableStateOf(null)

    init {
        val retrofit: Retrofit = Retrofit
            .Builder()
            .baseUrl("https://restaurants-db-d98a6-default-rtdb.europe-west1.firebasedatabase.app")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        restInterface = retrofit.create(RestaurantsApiService::class.java)

        val id = stateHandle.get<Int>("restaurant_id") ?: 0
        viewModelScope.launch {
            val restaurantDetails = getRestaurantDetails(id)
            state.value = restaurantDetails
        }
    }

    private suspend fun getRestaurantDetails(id: Int): Restaurant {
        return withContext(Dispatchers.IO) {
            val response = restInterface.getRestaurant(id)
            return@withContext response.values.first()
        }
    }
}