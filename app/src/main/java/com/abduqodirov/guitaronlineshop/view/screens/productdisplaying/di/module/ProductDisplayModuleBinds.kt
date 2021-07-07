package com.abduqodirov.guitaronlineshop.view.screens.productdisplaying.di.module

import com.abduqodirov.guitaronlineshop.data.repository.fetching.ProductsFetchingRepository
import com.abduqodirov.guitaronlineshop.data.repository.fetching.ProductsFetchingRepositoryImpl
import com.abduqodirov.guitaronlineshop.view.screens.submitnewproduct.di.component.FragmentScope
import dagger.Binds
import dagger.Module

@Module
abstract class ProductDisplayModuleBinds {

    @FragmentScope
    @Binds
    abstract fun bindFetchingRepository(repo: ProductsFetchingRepositoryImpl): ProductsFetchingRepository
}
