package com.abduqodirov.guitaronlineshop.di.module

import com.abduqodirov.guitaronlineshop.data.repository.ProductsRepository
import com.abduqodirov.guitaronlineshop.data.repository.ProductsRepositoryImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class ProductsModuleBinds {

    @Singleton
    @Binds
    abstract fun bindRepository(repo: ProductsRepositoryImpl): ProductsRepository
}
