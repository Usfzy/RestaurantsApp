package com.usfzy.restaurantsapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.usfzy.restaurantsapp.model.PartialRestaurant
import com.usfzy.restaurantsapp.model.Restaurant

@Dao
interface RestaurantsDao {

    @Query("SELECT * FROM restaurants")
    suspend fun getAll(): List<Restaurant>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAll(restaurants: List<Restaurant>)

    @Update(entity = Restaurant::class)
    suspend fun updateRestaurant(partialRestaurant: PartialRestaurant)

    @Update(entity = Restaurant::class)
    suspend fun updateAll(partialRestaurants: List<PartialRestaurant>)

    @Query("SELECT * FROM restaurants WHERE is_favorite = 1")
    suspend fun getAllFavorited(): List<Restaurant>
}