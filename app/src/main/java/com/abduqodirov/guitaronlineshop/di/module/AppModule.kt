package com.abduqodirov.guitaronlineshop.di.module

import android.content.Context
import com.abduqodirov.guitaronlineshop.data.TokenManager
import com.abduqodirov.guitaronlineshop.data.network.RemoteDataSource
import com.abduqodirov.guitaronlineshop.data.network.implementations.RemoteDataSourceImpl
import com.abduqodirov.guitaronlineshop.data.network.retrofit.ShopApi
import com.abduqodirov.guitaronlineshop.di.scopes.AppScope
import dagger.Module
import dagger.Provides

@Module
class AppModule {

    @AppScope
    @Provides
    fun provideRemoteDataSource(): RemoteDataSource {
        return RemoteDataSourceImpl(ShopApi.shopService)
    }

    // @AppScope
    // @Provides
    // fun provideContext(application: ShopApplication): Context {
    //     return application.applicationContext
    // }

    @AppScope
    @Provides
    fun provideTokenManager(context: Context) = TokenManager(context)
}
