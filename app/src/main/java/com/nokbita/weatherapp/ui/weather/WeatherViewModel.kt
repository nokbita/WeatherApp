package com.nokbita.weatherapp.ui.weather

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.nokbita.weatherapp.logic.Repository
import com.nokbita.weatherapp.logic.model.PlaceResponse
import com.nokbita.weatherapp.logic.model.WeatherResponse
import kotlin.math.log

class WeatherViewModel: ViewModel() {

    private val coordinatesLiveData = MutableLiveData<PlaceResponse.Coordinates>()
    fun refreshWeatherResponse(lng: String, lat: String) {
        coordinatesLiveData.value = PlaceResponse.Coordinates(lat, lng)
        Log.d("WeatherViewModel","${lng}, ${lat}")
    }
    val weatherResponseLiveData: LiveData<Result<WeatherResponse>> = coordinatesLiveData.switchMap {
        coordinates -> Repository.refreshWeatherResponse2(coordinates.lng, coordinates.lat)
    }

    var placeName2 = ""
    var coordinatesLng2 = ""
    var coordinatesLat2 = ""
}