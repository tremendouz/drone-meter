package com.example.dawid.dronemeterclient

import android.app.Application
import kotlin.properties.Delegates

/**
 * Created by dawid on 06.11.17.
 */
class AppSingleton : Application() {
    companion object {
        lateinit var instance : AppSingleton
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}