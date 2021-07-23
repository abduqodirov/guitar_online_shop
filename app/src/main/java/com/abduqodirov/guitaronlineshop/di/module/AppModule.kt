package com.abduqodirov.guitaronlineshop.di.module

import com.abduqodirov.guitaronlineshop.data.network.RemoteDataSource
import com.abduqodirov.guitaronlineshop.data.network.implementations.RemoteDataSourceImpl
import com.abduqodirov.guitaronlineshop.data.network.retrofit.ShopApi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideRemoteDataSource(): RemoteDataSource {
        return RemoteDataSourceImpl(ShopApi.shopService)
    }
}
