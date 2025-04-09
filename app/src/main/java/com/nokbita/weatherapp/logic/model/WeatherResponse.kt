package com.nokbita.weatherapp.logic.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    val realtime: RealtimeResponse.Realtime,
    val daily: DailyResponse.Daily
) {

    data class RealtimeResponse(
        val status: String,
        val result: Result
    ) {

        data class Result(
            val realtime: Realtime
        )

        data class Realtime(
            // 天气现象
            val skycon: String,
            // 体感温度
            @SerializedName("apparent_temperature") val apparentTemperature: Float,
            // 空气质量
            @SerializedName("air_quality") val airQuality: AirQuality
        )

        data class AirQuality(
            val description: Description
        )

        data class Description(
            // 国标 AQI
            val chn: String,
            val usa: String
        )
    }

    data class DailyResponse(
        val status: String,
        val result: Result
    ) {

        data class Result(
            val daily: Daily
        )

        data class Daily(
            val temperature: List<Temperature>,
            val skycon: List<Skycon>,
            @SerializedName("life_index") val lifeIndex: LifeIndex
        )

        data class Temperature(
            val date: String,
            val max: Float,
            val min: Float
        )

        data class Skycon(
            @SerializedName("value") val skycon: String
        )

        data class LifeIndex(
            val ultraviolet: List<LifeDescription>,
            val carWashing: List<LifeDescription>,
            val dressing: List<LifeDescription>,
            val coldRisk: List<LifeDescription>,
        )

        data class LifeDescription(
            val desc: String
        )
    }

}
