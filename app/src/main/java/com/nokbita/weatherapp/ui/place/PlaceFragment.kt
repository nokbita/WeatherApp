package com.nokbita.weatherapp.ui.place

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nokbita.weatherapp.MainActivity
import com.nokbita.weatherapp.R
import com.nokbita.weatherapp.isConnectedNet
import com.nokbita.weatherapp.logic.dao.PlaceDao
import com.nokbita.weatherapp.logic.model.PlaceResponse
import com.nokbita.weatherapp.ui.weather.WeatherActivity

class PlaceFragment: Fragment() {
    // 可封装
    val placeViewModel by lazy { ViewModelProvider(this).get(PlaceViewModel::class.java) }

    private lateinit var placeRecyclerViewAdapter: PlaceRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_place, container, false)
        // 此处为placeFragmentXML
        Log.d("PlaceFragment","01此处的view是：${view.id}")

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 猜测为placeFragmentXML，猜测正确！！
        Log.d("PlaceFragment","02此处的view是：${view.id}")

        // 每次启动时检查网络状态
        if (!isConnectedNet()) {
            Toast.makeText(activity, "网络未连接", Toast.LENGTH_SHORT).show()
            return
        }

        // 如果地点已经存储，则直接跳转到天气显示页面
        if (activity is MainActivity && PlaceDao.isPlaceSaved()) {
            val savedPlace = PlaceDao.getSavedPlace()
            WeatherActivity.startSelf(this.requireActivity(),savedPlace.placeName,
                savedPlace.coordinates.lng, savedPlace.coordinates.lat)
            activity?.finish()
            return
        }

        val linearLayoutManager = LinearLayoutManager(activity)
        placeRecyclerViewAdapter = PlaceRecyclerViewAdapter(this, placeViewModel.placeList)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        // 此处为recyclerView
        Log.d("PlaceFragment","recyclerView是：${recyclerView.id}")
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = placeRecyclerViewAdapter

        val searchPlace = view.findViewById<EditText>(R.id.searchPlaceEdit)
        val bgImageView = view.findViewById<ImageView>(R.id.bgImageView)

        searchPlace.addTextChangedListener { editable ->
            val query = editable.toString()

            if (!isConnectedNet()) {
                Toast.makeText(activity, "网络未连接", Toast.LENGTH_SHORT).show()
                return@addTextChangedListener
            }

            if (query.isNotEmpty()) {
                placeViewModel.searchPlaces(query)
            } else {
                recyclerView.visibility = View.GONE
                bgImageView.visibility = View.VISIBLE
                placeViewModel.placeList.clear()
            }
        }

        placeViewModel.places.observe(viewLifecycleOwner, Observer{ result ->
            val places: List<PlaceResponse.Place>? = result.getOrNull()
            Log.d("PlaceFragment", places.toString())
            if (places != null) {
                recyclerView.visibility = View.VISIBLE
                bgImageView.visibility = View.GONE
                placeViewModel.placeList.clear()
                placeViewModel.placeList.addAll(places)
                placeRecyclerViewAdapter.notifyDataSetChanged()
            } else {
                // 如果数据为空则说明发生了异常
                // 可封装
                Toast.makeText(activity, "地点信息获取失败", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
    }
}