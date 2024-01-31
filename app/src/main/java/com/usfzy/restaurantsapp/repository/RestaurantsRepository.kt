package com.usfzy.restaurantsapp.repository

import com.usfzy.restaurantsapp.RestaurantsApplication
import com.usfzy.restaurantsapp.database.RestaurantsDao
import com.usfzy.restaurantsapp.database.RestaurantsDb
import com.usfzy.restaurantsapp.model.PartialRestaurant
import com.usfzy.restaurantsapp.model.Restaurant
import com.usfzy.restaurantsapp.retrofit.RestaurantsApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.ConnectException
import java.net.UnknownHostException

class RestaurantsRepository {

    private val restInterface: RestaurantsApiService =
        Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://restaurants-db-d98a6-default-rtdb.europe-west1.firebasedatabase.app/")
            .build()
            .create(RestaurantsApiService::class.java)

    private val restaurantsDao: RestaurantsDao =
        RestaurantsDb.getDaoInstance(
            RestaurantsApplication.getContext()
        )

    suspend fun getAllRestaurants(): List<Restaurant> {
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
            return@withContext restaurantsDao.getAll().sortedBy { it.title }
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

    suspend fun toggleFavoriteRestaurant(id: Int, oldValue: Boolean): List<Restaurant> {
        return withContext(Dispatchers.IO) {
            restaurantsDao.updateRestaurant(
                PartialRestaurant(id = id, isFavorite = !oldValue)
            )
            return@withContext restaurantsDao.getAll()
        }
    }

}