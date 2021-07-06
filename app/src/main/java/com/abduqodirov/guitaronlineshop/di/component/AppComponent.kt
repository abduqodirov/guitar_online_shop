package com.abduqodirov.guitaronlineshop.di.component

import android.content.Context
import com.abduqodirov.guitaronlineshop.data.repository.fetching.ProductsFetchingRepository
import com.abduqodirov.guitaronlineshop.data.repository.submitting.SubmitProductRepository
import com.abduqodirov.guitaronlineshop.di.module.AppModule
import com.abduqodirov.guitaronlineshop.di.module.ProductViewModelModule
import com.abduqodirov.guitaronlineshop.di.module.ProductsModuleBinds
import com.abduqodirov.guitaronlineshop.di.module.ViewModelFactoryModule
import com.abduqodirov.guitaronlineshop.view.ui.productdetails.ProductDetailsFragment
import com.abduqodirov.guitaronlineshop.view.ui.productslist.ProductsListFragment
import com.abduqodirov.guitaronlineshop.view.ui.submitnewproduct.SubmitNewProductFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        ProductsModuleBinds::class,
        ViewModelFactoryModule::class,
        ProductViewModelModule::class,
    ]
)
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): AppComponent
    }

    fun inject(fragment: ProductsListFragment)

    fun inject(fragment: ProductDetailsFragment)

    fun inject(fragment: SubmitNewProductFragment)

    val productFetchingRepository: ProductsFetchingRepository

    val submitProductRepository: SubmitProductRepository
}
