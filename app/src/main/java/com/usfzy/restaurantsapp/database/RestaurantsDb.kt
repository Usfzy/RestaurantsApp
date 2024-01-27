package com.usfzy.restaurantsapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.usfzy.restaurantsapp.model.Restaurant

@Database(
    entities = [Restaurant::class], version = 1, exportSchema = false
)
abstract class RestaurantsDb : RoomDatabase() {

    abstract val dao: RestaurantsDao

    companion object {
        @Volatile
        private var INSTANCE: RestaurantsDao? = null

        fun getDaoInstance(context: Context): RestaurantsDao {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = buildDatabase(context).dao
                    INSTANCE = instance
                }
                return instance
            }
        }

        private fun buildDatabase(context: Context): RestaurantsDb {
            return Room.databaseBuilder(
                context.applicationContext,
                RestaurantsDb::class.java,
                "restaurants_database",
            ).fallbackToDestructiveMigration().build()
        }
    }
}