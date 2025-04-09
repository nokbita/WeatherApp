package com.nokbita.weatherapp.logic.network

import com.nokbita.weatherapp.WeatherAppApplication
import com.nokbita.weatherapp.logic.model.WeatherResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherResponseService {

    @GET("v2.6/${WeatherAppApplication.TOKEN_CAIYUN}/{lng},{lat}/realtime")
    fun getRealtimeResponse(@Path("lng") lng: String,
                             @Path("lat") lat: String
    ): Call<WeatherResponse.RealtimeResponse>


    @GET("v2.6/${WeatherAppApplication.TOKEN_CAIYUN}/{lng},{lat}/realtime")
    suspend fun getRealtimeResponse2(@Path("lng") lng: String,
                           @Path("lat") lat: String
    ): Response<WeatherResponse.RealtimeResponse>


    @GET("v2.6/${WeatherAppApplication.TOKEN_CAIYUN}/{lng},{lat}/realtime")
    suspend fun getRealtimeResponse3(@Path("lng") lng: String,
                             @Path("lat") lat: String
    ): WeatherResponse.RealtimeResponse


    @GET("v2.6/${WeatherAppApplication.TOKEN_CAIYUN}/{lng},{lat}/daily?")
    fun getDailyResponse(@Path("lng") lng: String,
                           @Path("lat") lat: String,
                         @Query("dailysteps") dailySteps: Int
    ): Call<WeatherResponse.DailyResponse>
}