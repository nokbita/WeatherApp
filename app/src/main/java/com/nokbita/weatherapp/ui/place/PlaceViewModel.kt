package com.nokbita.weatherapp.ui.place

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.nokbita.weatherapp.logic.Repository
import com.nokbita.weatherapp.logic.model.PlacesResponse

class PlaceViewModel : ViewModel() {

    val placeList = ArrayList<PlacesResponse.Place>()


    private val queryLiveData: MutableLiveData<String> = MutableLiveData<String>()

    val placeLiveData: LiveData<Result<List<PlacesResponse.Place>>> = queryLiveData.switchMap { query ->
        Repository.searchPlaces(query)
    }

    fun searchPlaces(query: String): Unit {
        queryLiveData.value = query
    }
}