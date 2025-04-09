package com.nokbita.weatherapp.ui.place

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.nokbita.weatherapp.logic.Repository
import com.nokbita.weatherapp.logic.model.PlaceResponse
import com.nokbita.weatherapp.logic.model.PlaceResponse.Place

class PlaceViewModel : ViewModel() {

    val placeList = ArrayList<PlaceResponse.Place>()

    private val queryLiveData = MutableLiveData<String>()
    val places: LiveData<Result<List<PlaceResponse.Place>>> = queryLiveData.switchMap { query ->
        Repository.searchPlaces(query)
    }
    fun searchPlaces(query: String): Unit {
        queryLiveData.value = query
    }

    private val savePlaceLiveData = MutableLiveData<Place>()
    val savePlaceResult: LiveData<Result<Boolean>> = savePlaceLiveData.switchMap { place ->
        // 测试结果：如果没有执行savePlaceResult的observe方法，则switchMap方法不会执行
        Log.d("PlaceViewModel","savePlaceLiveData的switchMap方法执行了吗")
        Repository.savePlace(place)
    }
    fun savePlace(place: Place) {
        savePlaceLiveData.value = place
    }
    fun savePlace2(place: Place) = Repository.savePlace(place)


    private val getSavedPlaceLiveData = MutableLiveData<Any?>()
    val getSavedPlaceResult: LiveData<Result<Place>> = getSavedPlaceLiveData.switchMap {
        Repository.getSavedPlace()
    }
    fun getSavedPlace() {
        getSavedPlaceLiveData.value = getSavedPlaceLiveData.value
    }
    fun getSavedPlace2(): LiveData<Result<PlaceResponse.Place>> = Repository.getSavedPlace()


    private val isPlaceSavedLiveData = MutableLiveData<Any?>()
    val isPlaceSavedResult: LiveData<Result<Boolean>> = isPlaceSavedLiveData.switchMap {
        Repository.isPlaceSaved()
    }
    fun isPlaceSaved() {
        isPlaceSavedLiveData.value = isPlaceSavedLiveData.value
    }
    fun isPlaceSaved2(): LiveData<Result<Boolean>> = Repository.isPlaceSaved()



}