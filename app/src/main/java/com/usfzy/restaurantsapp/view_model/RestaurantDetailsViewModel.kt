package com.usfzy.restaurantsapp.view_model

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.usfzy.restaurantsapp.repository.RestaurantDetailsRepository
import com.usfzy.restaurantsapp.state.RestaurantDetailsScreenState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class RestaurantDetailsViewModel(stateHandle: SavedStateHandle) : ViewModel() {

    private val repository = RestaurantDetailsRepository()
    private val _state = mutableStateOf(
        RestaurantDetailsScreenState(
            restaurant = null,
            isLoading = true,
        )
    )
    val state: State<RestaurantDetailsScreenState> get() = _state
    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
        _state.value = _state.value.copy(
            isLoading = false,
            error = throwable.message,
        )
    }

    init {
        val id = stateHandle.get<Int>("restaurant_id") ?: 0
        viewModelScope.launch(errorHandler) {
            val restaurantDetails = repository.getRestaurantDetails(id)
            _state.value = _state.value.copy(
                restaurant = restaurantDetails,
                isLoading = false,
            )
        }
    }
}