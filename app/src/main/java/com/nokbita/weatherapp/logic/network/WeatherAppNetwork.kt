package com.nokbita.weatherapp.logic.network

import com.nokbita.weatherapp.logic.model.PlacesResponse
import retrofit2.await

object WeatherAppNetwork {
    private val placesResponseService: PlacesResponseService =
        ServiceCreator.create<PlacesResponseService>()

    // 将searchPlaces声明成挂起函数，但不提供协程作用域。①挂起函数会挂起当前协程；②挂起函数只能在协程作用域或其他挂起函数中调用。
    suspend fun searchPlaces(query: String): PlacesResponse = placesResponseService
        .searchPlaces(query).await()

}