package com.nokbita.weatherapp

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

// 获取系统服务中的ConnectivityManager
private val connectivityManager = WeatherAppApplication.context
    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

fun isConnectedNet(): Boolean {
    // 获取当前活跃的网络信息
    val activeNetwork = connectivityManager.activeNetwork
    // 获取网络信息
    val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
    // 检查网络是否连接并且网络是否有效
    return networkCapabilities != null && networkCapabilities.hasCapability(
        NetworkCapabilities.NET_CAPABILITY_INTERNET)
}