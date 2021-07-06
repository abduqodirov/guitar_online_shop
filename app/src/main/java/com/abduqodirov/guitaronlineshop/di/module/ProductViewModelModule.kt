package com.abduqodirov.guitaronlineshop.di.module

import androidx.lifecycle.ViewModel
import com.abduqodirov.guitaronlineshop.view.ui.productdetails.ProductDetailsViewModel
import com.abduqodirov.guitaronlineshop.view.ui.productslist.ProductsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ProductViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ProductsViewModel::class)
    abstract fun bindsProductViewModel(productsViewModel: ProductsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProductDetailsViewModel::class)
    abstract fun bindsProductsDetailsViewModel(productDetailsViewModel: ProductDetailsViewModel): ViewModel
}
