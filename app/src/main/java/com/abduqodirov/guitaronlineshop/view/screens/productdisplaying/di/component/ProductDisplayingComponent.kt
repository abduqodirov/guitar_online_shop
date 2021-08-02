package com.abduqodirov.guitaronlineshop.view.screens.productdisplaying.di.component

import com.abduqodirov.guitaronlineshop.data.repository.fetching.ProductsFetchingRepository
import com.abduqodirov.guitaronlineshop.di.scopes.FragmentScope
import com.abduqodirov.guitaronlineshop.view.screens.productdisplaying.di.module.ProductDisplayModuleBinds
import com.abduqodirov.guitaronlineshop.view.screens.productdisplaying.di.module.ProductViewModelModule
import com.abduqodirov.guitaronlineshop.view.screens.productdisplaying.productdetails.ProductDetailsFragment
import com.abduqodirov.guitaronlineshop.view.screens.productdisplaying.productdetails.comments.CommentsFragment
import com.abduqodirov.guitaronlineshop.view.screens.productdisplaying.productslist.ProductsListFragment
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [ProductDisplayModuleBinds::class, ProductViewModelModule::class])
interface ProductDisplayingComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): ProductDisplayingComponent
    }

    val productFetchingRepository: ProductsFetchingRepository

    fun inject(fragment: ProductsListFragment)

    fun inject(fragment: ProductDetailsFragment)

    fun inject(fragment: CommentsFragment)
}
