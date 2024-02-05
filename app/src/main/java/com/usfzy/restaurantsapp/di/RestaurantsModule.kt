package com.usfzy.restaurantsapp.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.usfzy.restaurantsapp.database.RestaurantsDao
import com.usfzy.restaurantsapp.database.RestaurantsDb
import com.usfzy.restaurantsapp.retrofit.RestaurantsApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RestaurantsModule {

    @Provides
    fun provideRoomDao(database: RestaurantsDb): RestaurantsDao {
        return database.dao
    }

    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext context: Context): RestaurantsDb {
        return Room.databaseBuilder(
            context,
            RestaurantsDb::class.java,
            "restaurants_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://restaurants-db-d98a6-default-rtdb.europe-west1.firebasedatabase.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideRetrofitApiService(retrofit: Retrofit): RestaurantsApiService {
        return retrofit.create(RestaurantsApiService::class.java)
    }
}