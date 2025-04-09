package com.nokbita.weatherapp.ui.weather

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.nokbita.weatherapp.R
import com.nokbita.weatherapp.logic.model.WeatherResponse
import com.nokbita.weatherapp.logic.model.getSky
import java.text.SimpleDateFormat
import java.util.Locale

class WeatherActivity : AppCompatActivity() {

    // 可封装
    val weatherViewModel by lazy { ViewModelProvider(this).get(WeatherViewModel::class.java) }

    companion object {
        fun startSelf(
            context: Context,
            placeName: String,
            coordinatesLng: String,
            coordinatesLat: String
        ) {
            val intent = Intent(context, WeatherActivity::class.java).apply {
                putExtra("placeName", placeName)
                putExtra("coordinatesLng", coordinatesLng)
                putExtra("coordinatesLat", coordinatesLat)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_weather)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.weatherLayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 设置activity可在状态栏中显示，并将状态栏设置为透明
        val decorView = window.decorView
        decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.statusBarColor = Color.TRANSPARENT


        // 将搜索城市功能设置为抽屉
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayoutXML)
        findViewById<Button>(R.id.navBtn).setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        drawerLayout.addDrawerListener(object: DrawerLayout.DrawerListener{
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
            }

            override fun onDrawerOpened(drawerView: View) {
            }

            override fun onDrawerClosed(drawerView: View) {
                // 抽屉关闭时，收起输入法
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as
                        InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(drawerView.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS)
            }

            override fun onDrawerStateChanged(newState: Int) {
            }
        })

        // 如果placeName等变量中没有数据，则从intent中获取数据
        if (weatherViewModel.placeName.isEmpty() ||
            weatherViewModel.coordinatesLng.isEmpty() ||
            weatherViewModel.coordinatesLat.isEmpty()) {
            weatherViewModel.placeName = intent.getStringExtra("placeName") ?: ""
            weatherViewModel.coordinatesLng = intent.getStringExtra("coordinatesLng") ?: ""
            weatherViewModel.coordinatesLat = intent.getStringExtra("coordinatesLat") ?: ""
            Log.d("WeatherActivity, intent：","${intent.getStringExtra("coordinatesLng")}, " +
                    "${intent.getStringExtra("coordinatesLat")}, " +
                    "${intent.getStringExtra("placeName")}")
        }

        // 获取刷新组件
        val swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setColorSchemeResources(R.color.alex)
        refreshWeatherResponse(swipeRefreshLayout)

        // 监听刷新事件
        swipeRefreshLayout.setOnRefreshListener {
            refreshWeatherResponse(swipeRefreshLayout)
        }

        // 观察weatherResponseLiveData
        weatherViewModel.weatherResponseLiveData.observe(this) { result ->
            val weatherResponse = result.getOrNull()
            if (weatherResponse != null) {
                setUIByWeather(weatherResponse)
            } else {
                Toast.makeText(this, "天气信息获取失败", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
                Log.d("WeatherActivity: ","${result.exceptionOrNull()?.toString()}")
            }
            swipeRefreshLayout.isRefreshing = false
        }
    }

    // 天气刷新方法
    fun refreshWeatherResponse(swipeRefreshLayout: SwipeRefreshLayout) {
        // 获取天气信息
        weatherViewModel.refreshWeatherResponse(
            weatherViewModel.coordinatesLng,
            weatherViewModel.coordinatesLat
        )
        swipeRefreshLayout.isRefreshing = true
    }

    // 将weatherResponse展示在界面上
    fun setUIByWeather(weatherResponse: WeatherResponse) {
        val realtime = weatherResponse.realtime
        val daily = weatherResponse.daily

        // 设置realtime.xml
        findViewById<RelativeLayout>(R.id.realtimeXML)
            .setBackgroundResource(getSky(realtime.skycon).bg)
        findViewById<TextView>(R.id.placeName).text = weatherViewModel.placeName
        findViewById<TextView>(R.id.currentTemp).text =
            "${String.format("%.1f", realtime.apparentTemperature)} ℃"
        findViewById<TextView>(R.id.currentSkycon).text = getSky(realtime.skycon).info
        findViewById<TextView>(R.id.currentAQI).text =
            "空气质量 ${realtime.airQuality.description.chn}"

        // 设置forecast.xml
        val forecastLayout = findViewById<LinearLayout>(R.id.forecastLayout)
        forecastLayout.removeAllViews()
        for (i in 0 until daily.temperature.size) {
            val temperature = daily.temperature[i]
            val skycon = daily.skycon[i]

            // 获取item_forecast.xml
            val itemForecastView =
                LayoutInflater.from(this).inflate(R.layout.item_forecast, forecastLayout, false)
            val dateInfo = itemForecastView.findViewById<TextView>(R.id.dateInfo)
            val skyconIcon = itemForecastView.findViewById<ImageView>(R.id.skyconIcon)
            val skyconInfo = itemForecastView.findViewById<TextView>(R.id.skyconInfo)
            val temperatureInfo = itemForecastView.findViewById<TextView>(R.id.temperatureInfo)

            val dateStr: String = temperature.date
            Log.d("WeatherActivity, 日期：","${temperature.date}")
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            dateInfo.text = simpleDateFormat.format(simpleDateFormat.parse(dateStr))

            val sky = getSky(skycon.skycon)
            skyconIcon.setImageResource(sky.icon)
            skyconInfo.text = sky.info
            temperatureInfo.text = "${temperature.min.toInt()} ~ ${temperature.max.toInt()} ℃"

            // 将item_forecast.xml添加到forecast.xml的forecastLayout容器中
            forecastLayout.addView(itemForecastView)
        }

        // 设置life_index.xml
        val lifeIndex = daily.lifeIndex
        findViewById<TextView>(R.id.coldRiskText).text = lifeIndex.coldRisk[0].desc
        findViewById<TextView>(R.id.dressingText).text = lifeIndex.dressing[0].desc
        findViewById<TextView>(R.id.ultravioletText).text = lifeIndex.ultraviolet[0].desc
        findViewById<TextView>(R.id.carWashingText).text = lifeIndex.carWashing[0].desc

        // 显示activity_weather.xml
        findViewById<ScrollView>(R.id.weatherLayout).visibility = View.VISIBLE
    }

}


