package com.usfzy.restaurantsapp.repository

import com.usfzy.restaurantsapp.RestaurantsApplication
import com.usfzy.restaurantsapp.database.RestaurantsDao
import com.usfzy.restaurantsapp.database.RestaurantsDb
import com.usfzy.restaurantsapp.model.LocalRestaurant
import com.usfzy.restaurantsapp.model.PartialLocalRestaurant
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

    suspend fun getRestaurants(): List<Restaurant> {
        return withContext(Dispatchers.IO) {
            return@withContext restaurantsDao.getAll().map {
                Restaurant(it.id, it.title, it.description, it.isFavorite)
            }
        }
    }

    suspend fun loadRestaurants() {
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
        }
    }

    private suspend fun refreshCache() {
        val remoteRestaurants = restInterface.getRestaurants()
        val favoriteRestaurants = restaurantsDao.getAllFavorited()

        restaurantsDao.addAll(remoteRestaurants.map {
            LocalRestaurant(
                it.id, it.title, it.description, false,
            )
        })
        restaurantsDao.updateAll(
            favoriteRestaurants.map {
                PartialLocalRestaurant(it.id, true)
            }
        )
    }

    suspend fun toggleFavoriteRestaurant(id: Int, value: Boolean) {
        withContext(Dispatchers.IO) {
            restaurantsDao.updateRestaurant(
                PartialLocalRestaurant(id = id, isFavorite = value)
            )
        }
    }

}