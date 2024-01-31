package com.usfzy.restaurantsapp.view_model

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.usfzy.restaurantsapp.repository.RestaurantsRepository
import com.usfzy.restaurantsapp.state.RestaurantsScreenState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class RestaurantViewModel() : ViewModel() {
    private val restaurantsRepository = RestaurantsRepository()

    private val _state = mutableStateOf(
        RestaurantsScreenState(
            restaurants = listOf(),
            isLoading = true,
        )
    )
    val state: State<RestaurantsScreenState> get() = _state

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
        _state.value = _state.value.copy(error = throwable.message, isLoading = false)
    }


    init {
        getRestaurants()
    }

    private fun getRestaurants() {
        viewModelScope.launch(errorHandler) {// LAUNCH COROUTINE ON IO THREAD
            val restaurants = restaurantsRepository.getAllRestaurants()
            _state.value = _state.value.copy(
                restaurants = restaurants,
                isLoading = false,
            )
        }
    }


    fun toggleFavorite(id: Int, oldValue: Boolean) {
        viewModelScope.launch {
            val updatedRestaurants = restaurantsRepository.toggleFavoriteRestaurant(id, oldValue)
            _state.value = _state.value.copy(restaurants = updatedRestaurants)
        }
    }

}