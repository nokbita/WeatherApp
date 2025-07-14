package com.nokbita.weatherapp

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class WeatherAppApplication : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        const val TOKEN_CAIYUN = "5Xvxxxxxx"
        const val LANGUAGE = "zh_CN"
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}
