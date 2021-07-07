package com.abduqodirov.guitaronlineshop.view.screens.productdisplaying.di

import com.abduqodirov.guitaronlineshop.data.repository.fetching.ProductsFetchingRepository
import com.abduqodirov.guitaronlineshop.view.screens.productdisplaying.productdetails.ProductDetailsFragment
import com.abduqodirov.guitaronlineshop.view.screens.productdisplaying.productslist.ProductsListFragment
import com.abduqodirov.guitaronlineshop.view.screens.submitnewproduct.di.FragmentScope
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [ProductDisplayModuleBinds::class, ProductViewModelModule::class])
interface ProductDisplayingComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): ProductDisplayingComponent
    }

    fun inject(fragment: ProductsListFragment)

    fun inject(fragment: ProductDetailsFragment)

    val productFetchingRepository: ProductsFetchingRepository
}
