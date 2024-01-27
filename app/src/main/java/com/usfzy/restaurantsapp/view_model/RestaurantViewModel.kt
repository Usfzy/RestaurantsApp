package com.usfzy.restaurantsapp.view_model

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.usfzy.restaurantsapp.RestaurantsApplication
import com.usfzy.restaurantsapp.database.RestaurantsDao
import com.usfzy.restaurantsapp.database.RestaurantsDb
import com.usfzy.restaurantsapp.model.PartialRestaurant
import com.usfzy.restaurantsapp.model.Restaurant
import com.usfzy.restaurantsapp.retrofit.RestaurantsApiService
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.ConnectException
import java.net.UnknownHostException

class RestaurantViewModel() : ViewModel() {
    val state = mutableStateOf(emptyList<Restaurant>())
    private var restInterface: RestaurantsApiService

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }

    private val restaurantsDao: RestaurantsDao =
        RestaurantsDb.getDaoInstance(
            RestaurantsApplication.getContext()
        )

    init {
        val retrofit: Retrofit =
            Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://restaurants-db-d98a6-default-rtdb.europe-west1.firebasedatabase.app/")
                .build()

        restInterface = retrofit.create(RestaurantsApiService::class.java)

        getRestaurants()
    }

    private fun getRestaurants() {
        viewModelScope.launch(errorHandler) {// LAUNCH COROUTINE ON IO THREAD
            state.value = getAllRestaurants()
        }
    }

    private suspend fun getAllRestaurants(): List<Restaurant> {
        return withContext(Dispatchers.IO) {
            try {
                refreshCache()
            } catch (e: Exception) {
                when (e) {
                    is UnknownHostException,
                    is ConnectException,
                    is HttpException -> {
                        if (restaurantsDao.getAll().isEmpty())
                            throw Exception("Something went wrong")
                    }

                    else -> throw e
                }

            }
            return@withContext restaurantsDao.getAll()
        }
    }

    private suspend fun refreshCache() {
        val remoteRestaurants = restInterface.getRestaurants()
        val favoriteRestaurants = restaurantsDao.getAllFavorited()

        restaurantsDao.addAll(remoteRestaurants)
        restaurantsDao.updateAll(
            favoriteRestaurants.map {
                PartialRestaurant(it.id, true)
            }
        )
    }

    fun toggleFavorite(id: Int, oldValue: Boolean) {
        viewModelScope.launch {
            val updatedRestaurants = toggleFavoriteRestaurant(id, oldValue)
            state.value = updatedRestaurants
        }
    }

    private suspend fun toggleFavoriteRestaurant(id: Int, oldValue: Boolean): List<Restaurant> {
        return withContext(Dispatchers.IO) {
            restaurantsDao.updateRestaurant(
                PartialRestaurant(id = id, isFavorite = !oldValue)
            )
            return@withContext restaurantsDao.getAll()
        }
    }

    companion object {
        private const val FAVORITES = "favorites"
    }

}