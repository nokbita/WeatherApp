package com.nokbita.weatherapp.logic.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ServiceCreator {
    private const val CAIYUN_URL: String = "https://api.caiyunapp.com/"
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(CAIYUN_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // 由于内联函数会在编译时展开到调用它的地方，所以必须先将private对象暴露成public对象。
    fun <T> ____create(service: Class<T>): T = retrofit.create<T>(service)
    inline fun <reified T> create(): T = ____create<T>(T::class.java)
}