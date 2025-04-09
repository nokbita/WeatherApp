package com.nokbita.weatherapp.logic.network

import com.nokbita.weatherapp.logic.model.PlaceResponse
import com.nokbita.weatherapp.logic.model.WeatherResponse
import retrofit2.await

object WeatherAppNetwork {
    private val placesResponseService: PlacesResponseService =
        ServiceCreator.create<PlacesResponseService>()

    private val weatherResponseService: WeatherResponseService =
        ServiceCreator.create<WeatherResponseService>()

    // 将searchPlaces声明成挂起函数，但不提供协程作用域。①挂起函数会挂起当前协程；②挂起函数只能在协程作用域或其他挂起函数中调用。
    suspend fun getPlaceResponse(query: String): PlaceResponse = placesResponseService
        .getPlaceResponse(query).await()

    suspend fun getRealtimeResponse(lng: String, lat: String): WeatherResponse.RealtimeResponse =
        weatherResponseService.getRealtimeResponse(lng, lat).await()
    suspend fun getRealtimeResponse2(lng: String, lat: String): WeatherResponse.RealtimeResponse =
        weatherResponseService.getRealtimeResponse2(lng, lat).body()!!
    suspend fun getRealtimeResponse3(lng: String, lat: String): WeatherResponse.RealtimeResponse =
        weatherResponseService.getRealtimeResponse3(lng, lat)

    suspend fun getDailyResponse(lng: String, lat: String, dailySteps: Int): WeatherResponse.DailyResponse =
        weatherResponseService.getDailyResponse(lng, lat, dailySteps).await()


}