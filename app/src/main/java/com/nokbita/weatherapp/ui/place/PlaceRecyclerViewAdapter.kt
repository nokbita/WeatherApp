package com.nokbita.weatherapp.ui.place


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.nokbita.weatherapp.R
import com.nokbita.weatherapp.logic.model.PlacesResponse

class PlaceRecyclerViewAdapter(private val fragment: Fragment,
                               private val placeList: List<PlacesResponse.Place>
) : RecyclerView.Adapter<PlaceRecyclerViewAdapter.viewHolder>(){

    inner class viewHolder(itemRecyclerViewPlace: View) : RecyclerView.ViewHolder(itemRecyclerViewPlace) {
        val placeName = itemRecyclerViewPlace.findViewById<TextView>(R.id.placeName)
        val placeAddress = itemRecyclerViewPlace.findViewById<TextView>(R.id.placeAddress)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaceRecyclerViewAdapter.viewHolder {
        val itemRecyclerViewPlace = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recyclerview_place, parent, false)
        // 预测：recyclerView
        Log.d("PlaceRecyclerViewAdapter","检查parent变量到底是谁：${parent.id}")
        return viewHolder(itemRecyclerViewPlace)
    }

    override fun onBindViewHolder(
        holder: PlaceRecyclerViewAdapter.viewHolder,
        position: Int
    ) {
        val place = placeList[position]
        holder.placeName.text = place.placeName
        holder.placeAddress.text = place.formattedAddress
    }

    override fun getItemCount(): Int {
        return placeList.size
    }

}