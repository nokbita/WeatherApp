package com.nokbita.weatherapp.logic

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.nokbita.weatherapp.logic.model.PlacesResponse
import com.nokbita.weatherapp.logic.network.WeatherAppNetwork
import kotlinx.coroutines.Dispatchers

object Repository {
    // ①liveData提供一个挂起函数的上下文。
    // ②liveData提供一个子线程的运行环境。Dispatchers.IO指定线程的参数，该值表示高并发的线程策略。
    fun searchPlaces(query: String): LiveData<Result<List<PlacesResponse.Place>>> = liveData(Dispatchers.IO) {
        Log.d("Repository", "liveData函数体")
        val result = try {
            val placesResponse = WeatherAppNetwork.searchPlaces(query)
            Log.d("Repository", placesResponse.toString())
            if (placesResponse.status == "ok") {
                val places = placesResponse.places
                Result.success<List<PlacesResponse.Place>>(places)
            } else {
                Result.failure(RuntimeException("网络响应状态为失败"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
        emit(result)
    }
}