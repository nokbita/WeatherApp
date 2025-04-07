package com.nokbita.weatherapp

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class WeatherAppApplication : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        const val TOKEN_CAIYUN = "5XvQj6lTcukm6Iaj"
        const val LANGUAGE = "zh_CN"
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}