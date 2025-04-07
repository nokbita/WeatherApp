package com.nokbita.weatherapp.logic.model

import com.google.gson.annotations.SerializedName


// 该实体类表示地点
data class PlacesResponse(
    val status: String,
    val query: String,
    val places: List<Place>) {

    data class Place(
        @SerializedName("formatted_address")val formattedAddress: String,
        @SerializedName("name") val placeName: String,
        @SerializedName("location") val coordinates: Coordinates
    )

    data class Coordinates(
        // 纬度，latitude
        val lat: String,
        // 经度，longitude
        val lng: String,
    )
}