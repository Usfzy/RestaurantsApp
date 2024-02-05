package com.usfzy.restaurantsapp.view_model

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.usfzy.restaurantsapp.state.RestaurantsScreenState
import com.usfzy.restaurantsapp.usecases.GetInitialRestaurantsUseCase
import com.usfzy.restaurantsapp.usecases.ToggleRestaurantUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestaurantViewModel @Inject constructor(
    private val getRestaurantsUseCase: GetInitialRestaurantsUseCase,
    private val toggleRestaurantUseCase: ToggleRestaurantUseCase,
) : ViewModel() {

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
            val restaurants = getRestaurantsUseCase()
            _state.value = _state.value.copy(
                restaurants = restaurants,
                isLoading = false,
            )
        }
    }


    fun toggleFavorite(id: Int, oldValue: Boolean) {
        viewModelScope.launch {
            val updatedRestaurants = toggleRestaurantUseCase(id, oldValue)
            _state.value = _state.value.copy(restaurants = updatedRestaurants)
        }
    }

}