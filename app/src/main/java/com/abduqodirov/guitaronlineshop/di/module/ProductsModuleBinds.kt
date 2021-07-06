package com.abduqodirov.guitaronlineshop.di.module

import com.abduqodirov.guitaronlineshop.data.repository.fetching.ProductsFetchingRepository
import com.abduqodirov.guitaronlineshop.data.repository.fetching.ProductsFetchingRepositoryImpl
import com.abduqodirov.guitaronlineshop.data.repository.submitting.SubmitProductRepository
import com.abduqodirov.guitaronlineshop.data.repository.submitting.SubmitProductRepositoryImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class ProductsModuleBinds {

    @Singleton
    @Binds
    abstract fun bindFetchingRepository(repo: ProductsFetchingRepositoryImpl): ProductsFetchingRepository

    @Singleton
    @Binds
    abstract fun bindSubmitRepository(repo: SubmitProductRepositoryImpl): SubmitProductRepository
}
