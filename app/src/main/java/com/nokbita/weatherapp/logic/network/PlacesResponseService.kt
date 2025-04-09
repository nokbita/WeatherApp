package com.nokbita.weatherapp.logic.network

import com.nokbita.weatherapp.WeatherAppApplication
import com.nokbita.weatherapp.logic.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesResponseService {

    @GET("v2/place?" +
            "token=${WeatherAppApplication.TOKEN_CAIYUN}&lang=${WeatherAppApplication.LANGUAGE}")
    fun getPlaceResponse(@Query("query") query: String): Call<PlaceResponse>
}