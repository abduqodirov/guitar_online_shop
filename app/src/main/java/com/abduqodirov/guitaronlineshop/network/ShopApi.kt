package com.abduqodirov.guitaronlineshop.network

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

const val BASE_URL = "http://192.168.1.103:3000"

val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create())
    .build()

object ShopApi {
    val shopService: ShopService by lazy {
        retrofit.create(ShopService::class.java)
    }
}
