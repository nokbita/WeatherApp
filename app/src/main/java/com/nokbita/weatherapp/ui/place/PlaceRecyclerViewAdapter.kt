package com.nokbita.weatherapp.ui.place


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nokbita.weatherapp.R
import com.nokbita.weatherapp.logic.Repository
import com.nokbita.weatherapp.logic.dao.PlaceDao
import com.nokbita.weatherapp.logic.model.PlaceResponse
import com.nokbita.weatherapp.ui.weather.WeatherActivity

class PlaceRecyclerViewAdapter(private val fragment: PlaceFragment,
                               private val placeList: List<PlaceResponse.Place>
) : RecyclerView.Adapter<PlaceRecyclerViewAdapter.ViewHolder>(){

    inner class ViewHolder(itemRecyclerViewPlace: View) : RecyclerView.ViewHolder(itemRecyclerViewPlace) {
        val placeName = itemRecyclerViewPlace.findViewById<TextView>(R.id.placeName)
        val placeAddress = itemRecyclerViewPlace.findViewById<TextView>(R.id.placeAddress)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaceRecyclerViewAdapter.ViewHolder {
        Log.d("PlaceRecyclerViewAdapter","检查parent变量到底是谁：${parent.id}")  // 预测：recyclerView，预测正确！！
        Log.d("PlaceRecyclerViewAdapter","检查parent.context与fragment.activity的关系：\n" +
                "${parent.context},\n" +
                "${fragment.activity}")

        val itemPlaceRecyclerView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_place_recyclerview, parent, false)
        val viewHolder = ViewHolder(itemPlaceRecyclerView)

        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            val place = placeList[position]
            Log.d("PlaceRecyclerViewAdapter,place: ", "${place}")

            // 存储地点
            PlaceDao.savePlace(place)
            Log.d("PlaceRecyclerViewAdapter","地点已存储")

            // 启动天气页面
            WeatherActivity.startSelf(
                parent.context,
                place.placeName,
                place.coordinates.lng,
                place.coordinates.lat)
            fragment.activity?.finish()
        }
        return viewHolder
    }

    override fun onBindViewHolder(
        holder: PlaceRecyclerViewAdapter.ViewHolder,
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