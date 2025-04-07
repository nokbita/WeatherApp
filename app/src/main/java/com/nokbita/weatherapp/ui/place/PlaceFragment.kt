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
import com.nokbita.weatherapp.R
import com.nokbita.weatherapp.isConnectedNet
import com.nokbita.weatherapp.logic.model.PlacesResponse

class PlaceFragment: Fragment() {
    // 可封装
    val viewModel by lazy { ViewModelProvider(this).get(PlaceViewModel::class.java) }

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

        val linearLayoutManager = LinearLayoutManager(activity)
        placeRecyclerViewAdapter = PlaceRecyclerViewAdapter(this, viewModel.placeList)
        // 猜测为placeFragmentXML
        Log.d("PlaceFragment","02此处的view是：${view.id}")
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
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
                viewModel.searchPlaces(query)
            } else {
                recyclerView.visibility = View.GONE
                bgImageView.visibility = View.VISIBLE
                viewModel.placeList.clear()
            }
        }

        viewModel.placeLiveData.observe(viewLifecycleOwner, Observer{ result ->
            val places: List<PlacesResponse.Place>? = result.getOrNull()
            Log.d("PlaceFragment", places.toString())
            if (places != null) {
                recyclerView.visibility = View.VISIBLE
                bgImageView.visibility = View.GONE
                viewModel.placeList.clear()
                viewModel.placeList.addAll(places)
                placeRecyclerViewAdapter.notifyDataSetChanged()
            } else {
                // 如果数据为空则说明发生了异常
                // 可封装
                Toast.makeText(activity, "未查询到任何地点", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
    }
}