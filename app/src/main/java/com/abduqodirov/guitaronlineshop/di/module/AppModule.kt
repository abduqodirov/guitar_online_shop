package com.abduqodirov.guitaronlineshop.di.module

import com.abduqodirov.guitaronlineshop.data.network.IRemoteDataSource
import com.abduqodirov.guitaronlineshop.data.network.implementations.RemoteDataSourceImpl
import com.abduqodirov.guitaronlineshop.data.network.retrofit.ShopApi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideRemoteDataSource(): IRemoteDataSource {
        return RemoteDataSourceImpl(ShopApi.shopService)
    }
}
