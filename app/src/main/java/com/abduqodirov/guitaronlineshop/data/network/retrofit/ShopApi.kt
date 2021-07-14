package com.abduqodirov.guitaronlineshop.data.network.retrofit

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

const val BASE_URL = "http://daac4d6ae50b.ngrok.io"

val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create())
    .build()

object ShopApi {
    val shopService: ShopService by lazy {
        retrofit.create(ShopService::class.java)
    }
}
