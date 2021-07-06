package com.abduqodirov.guitaronlineshop.di.module

import com.abduqodirov.guitaronlineshop.data.network.BASE_URL
import com.abduqodirov.guitaronlineshop.data.network.IRemoteDataSource
import com.abduqodirov.guitaronlineshop.data.network.RemoteDataSourceImpl
import com.abduqodirov.guitaronlineshop.data.network.ShopService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideRemoteDataSource(): IRemoteDataSource {
        return RemoteDataSourceImpl(providerRetrofitService())
    }

    private fun providerRetrofitService(): ShopService {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        return retrofit.create(ShopService::class.java)
    }
}
