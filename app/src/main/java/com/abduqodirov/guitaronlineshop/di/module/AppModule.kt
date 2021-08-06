package com.abduqodirov.guitaronlineshop.di.module

import android.content.Context
import com.abduqodirov.guitaronlineshop.data.local_chaching.UserManager
import com.abduqodirov.guitaronlineshop.data.network.RemoteDataSource
import com.abduqodirov.guitaronlineshop.data.network.implementations.RemoteDataSourceImpl
import com.abduqodirov.guitaronlineshop.data.network.retrofit.ShopService
import com.abduqodirov.guitaronlineshop.di.scopes.AppScope
import dagger.Module
import dagger.Provides

@Module
class AppModule {

    @AppScope
    @Provides
    fun provideRemoteDataSource(shopService: ShopService): RemoteDataSource = RemoteDataSourceImpl(shopService)

    @AppScope
    @Provides
    fun provideUserManager(context: Context): UserManager = UserManager(context)
}
