package com.abduqodirov.guitaronlineshop.di.module

import android.content.Context
import com.abduqodirov.guitaronlineshop.data.local_chaching.UserManager
import com.abduqodirov.guitaronlineshop.data.network.retrofit.BASE_URL
import com.abduqodirov.guitaronlineshop.data.network.retrofit.ServiceInterceptor
import com.abduqodirov.guitaronlineshop.data.network.retrofit.ShopService
import com.abduqodirov.guitaronlineshop.di.scopes.AppScope
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
class NetworkModule {

    @AppScope
    @Provides
    fun provideOkHttpClient(userManager: UserManager, context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(ChuckerInterceptor(context = context))
            .addInterceptor(ServiceInterceptor(userManager))
            .build()
    }

    @AppScope
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    @AppScope
    @Provides
    fun shopService(retrofit: Retrofit): ShopService {
        return retrofit.create(ShopService::class.java)
    }
}
