package com.usfzy.restaurantsapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.usfzy.restaurantsapp.model.LocalRestaurant

@Database(
    entities = [LocalRestaurant::class],
    version = 2,
    exportSchema = false
)
abstract class RestaurantsDb : RoomDatabase() {
    abstract val dao: RestaurantsDao
}