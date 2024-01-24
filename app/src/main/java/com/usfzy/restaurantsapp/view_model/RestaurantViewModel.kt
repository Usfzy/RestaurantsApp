package com.usfzy.restaurantsapp.view_model

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.usfzy.restaurantsapp.model.Restaurant
import com.usfzy.restaurantsapp.retrofit.RestaurantsApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RestaurantViewModel(private val stateHandle: SavedStateHandle) : ViewModel() {
    val state = mutableStateOf(emptyList<Restaurant>())
    private var restInterface: RestaurantsApiService

    private val job = Job()
    private val scope = CoroutineScope(context = job + Dispatchers.IO)

    init {
        val retrofit: Retrofit = Retrofit
            .Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://restaurants-db-d98a6-default-rtdb.europe-west1.firebasedatabase.app/")
            .build()

        restInterface = retrofit.create(RestaurantsApiService::class.java);

        getRestaurants()
    }

    private fun getRestaurants() {

        scope.launch {// LAUNCH COROUTINE ON IO THREAD
            val restaurants = restInterface.getRestaurants()
            
            withContext(Dispatchers.Main) {
                state.value = restaurants
            } // UPDATE UI ON MAIN THREAD
        }
    }

    fun toggleFavorite(id: Int) {
        val restaurants = state.value.toMutableList()
        val itemIndex = restaurants.indexOfFirst { it.id == id }

        val item = restaurants[itemIndex]
        restaurants[itemIndex] = item.copy(isFavorite = !item.isFavorite)

        storeSelection(restaurants[itemIndex])

        state.value = restaurants
    }

    private fun storeSelection(restaurant: Restaurant) {
        val savedToggled = stateHandle
            .get<List<Int>?>(FAVORITES)
            .orEmpty().toMutableList()

        if (restaurant.isFavorite) savedToggled.add(restaurant.id)
        else savedToggled.remove(restaurant.id)

        stateHandle[FAVORITES] = savedToggled
    }

    companion object {
        private const val FAVORITES = "favorites"
    }

    private fun List<Restaurant>.restoreSelection(): List<Restaurant> {

        stateHandle.get<List<Int>?>(FAVORITES)?.let { selectedIds ->
            val restaurantsMap = this.associateBy { it.id }
            selectedIds.forEach { id ->
                restaurantsMap[id]?.isFavorite = true
            }
            return restaurantsMap.values.toList()
        }
        return this
    }

    override fun onCleared() {
        super.onCleared()

        job.cancel()
    }
}