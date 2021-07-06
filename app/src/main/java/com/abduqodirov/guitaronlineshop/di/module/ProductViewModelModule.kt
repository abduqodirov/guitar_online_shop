package com.abduqodirov.guitaronlineshop.di.module

import androidx.lifecycle.ViewModel
import com.abduqodirov.guitaronlineshop.view.screens.productdetails.ProductDetailsViewModel
import com.abduqodirov.guitaronlineshop.view.screens.productslist.ProductsViewModel
import com.abduqodirov.guitaronlineshop.view.screens.submitnewproduct.SubmitProductViewModel
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
    abstract fun bindsProductsDetailsViewModel(
        productDetailsViewModel: ProductDetailsViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SubmitProductViewModel::class)
    abstract fun bindsSubmitProductViewModel(
        submitProductViewModel: SubmitProductViewModel
    ): ViewModel
}
