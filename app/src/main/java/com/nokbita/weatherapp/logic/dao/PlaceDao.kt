package com.nokbita.weatherapp.logic.dao

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.nokbita.weatherapp.WeatherAppApplication
import com.nokbita.weatherapp.logic.model.PlaceResponse.Place

object PlaceDao {

    fun savePlace(place: Place) {
        getSharedPreferences().edit {
            putString("place", Gson().toJson(place))
        }
    }

    fun getSavedPlace(): Place {
        val placeJson = getSharedPreferences().getString("place", "")
        return Gson().fromJson<Place>(placeJson, Place::class.java)
    }

    fun isPlaceSaved(): Boolean = getSharedPreferences().contains("place")

    private fun getSharedPreferences() =
        WeatherAppApplication.context.getSharedPreferences("weatherApp", Context.MODE_PRIVATE)
}