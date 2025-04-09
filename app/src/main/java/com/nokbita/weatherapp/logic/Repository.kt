package com.nokbita.weatherapp.logic

import android.util.Log
import androidx.annotation.experimental.Experimental
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.nokbita.weatherapp.logic.dao.PlaceDao
import com.nokbita.weatherapp.logic.model.PlaceResponse
import com.nokbita.weatherapp.logic.model.WeatherResponse
import com.nokbita.weatherapp.logic.network.WeatherAppNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.CoroutineContext

object Repository {


    // Dispatchers.Default: 该值表示默认低并发的线程策略。
    fun savePlace(place: PlaceResponse.Place): LiveData<Result<Boolean>> = fire(Dispatchers.Default) {
        PlaceDao.savePlace(place)
        Result.success(true)
    }

    fun getSavedPlace(): LiveData<Result<PlaceResponse.Place>> = fire(Dispatchers.Default) {
        val savedPlace = PlaceDao.getSavedPlace()
        Result.success(savedPlace)
    }

    fun isPlaceSaved(): LiveData<Result<Boolean>> = fire(Dispatchers.Default) {
        Result.success(PlaceDao.isPlaceSaved())
    }

    // ①liveData提供一个挂起函数的上下文。
    // ②liveData提供一个子线程的运行环境。Dispatchers.IO指定线程的参数，该值表示高并发的线程策略。
    fun searchPlaces(query: String): LiveData<Result<List<PlaceResponse.Place>>> =
        liveData(Dispatchers.IO) {
            Log.d("Repository", "liveData函数体")
            val result = try {
                val placesResponse = WeatherAppNetwork.getPlaceResponse(query)
                Log.d("Repository", placesResponse.toString())
                if (placesResponse.status == "ok") {
                    val places = placesResponse.places
                    Result.success<List<PlaceResponse.Place>>(places)
                } else {
                    Result.failure(
                        RuntimeException("placesResponse.status = ${placesResponse.status}")
                    )
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
            emit(result)
    }

    fun refreshWeatherResponse(lng: String, lat: String, dailySteps: Int = 5): LiveData<Result<WeatherResponse>> =
        liveData(Dispatchers.IO) {
            val result = try {
                coroutineScope {
                    val deferredRealtime = async {
                        WeatherAppNetwork.getRealtimeResponse(lng, lat)
                    }
                    val deferredDaily = async {
                        WeatherAppNetwork.getDailyResponse(lng, lat, dailySteps)
                    }
                    val realtimeResponse = deferredRealtime.await()
                    val dailyResponse = deferredDaily.await()

                    Log.d("Repository, realtimeResponse:", "${realtimeResponse}")
                    Log.d("Repository, dailyResponse:", "${dailyResponse}")

                    if (realtimeResponse.status == "ok" && dailyResponse.status == "ok") {
                        val weatherResponse = WeatherResponse(realtimeResponse.result.realtime,
                            dailyResponse.result.daily)
                        Result.success<WeatherResponse>(weatherResponse)
                    } else {
                        Log.d("Repository, realtimeResponse2:", "${realtimeResponse}")
                        Log.d("Repository, dailyResponse2:", "${dailyResponse}")
                        Result.failure(
                            RuntimeException("realtimeResponse.status = ${realtimeResponse.status}," +
                                    "dailyResponse.status = ${dailyResponse.status}")
                        )
                    }
                }
            } catch (e: Exception) {
                Log.d("Repository, realtimeResponse3", "${3}")
                Log.d("Repository, dailyResponse3:", "${3}")
                Result.failure(e)
            }
            emit(result)
    }

    fun refreshWeatherResponse2(lng: String, lat: String, dailySteps: Int = 5): LiveData<Result<WeatherResponse>> =
        fire(Dispatchers.IO){
            coroutineScope {
                val deferredRealtime = async {
                    WeatherAppNetwork.getRealtimeResponse3(lng, lat)
                }
                val deferredDaily = async {
                    WeatherAppNetwork.getDailyResponse(lng, lat, dailySteps)
                }
                val realtimeResponse = deferredRealtime.await()
                val dailyResponse = deferredDaily.await()

                if (realtimeResponse.status == "ok" && dailyResponse.status == "ok") {
                    val weatherResponse = WeatherResponse(realtimeResponse.result.realtime,
                        dailyResponse.result.daily)
                    Result.success<WeatherResponse>(weatherResponse)
                } else {
                    Result.failure(
                        RuntimeException("realtimeResponse.status = ${realtimeResponse.status}," +
                                "dailyResponse.status = ${dailyResponse.status}")
                    )
                }
            }
        }


    /**
     * public fun <T> liveData(
     *     context: CoroutineContext = EmptyCoroutineContext,
     *     timeoutInMs: Long = DEFAULT_TIMEOUT,
     *     block: suspend LiveDataScope<T>.() -> Unit
     * ): LiveData<T> = CoroutineLiveData(context, timeoutInMs, block)
     */

    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>):
        LiveData<Result<T>> = liveData<Result<T>>(context){
            val result = try {
                block()
            } catch(e: Exception) {
                Result.failure<T>(e)
            }
            emit(result)
    }
}