package com.usfzy.restaurantsapp

import android.app.Application
import android.content.Context

class RestaurantsApplication : Application() {

    init {
        app = this
    }

    companion object {
        private lateinit var app: RestaurantsApplication
        fun getContext(): Context = app.applicationContext
    }
}