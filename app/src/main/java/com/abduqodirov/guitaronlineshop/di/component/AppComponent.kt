package com.abduqodirov.guitaronlineshop.di.component

import android.content.Context
import com.abduqodirov.guitaronlineshop.di.module.AppModule
import com.abduqodirov.guitaronlineshop.di.module.ViewModelFactoryModule
import com.abduqodirov.guitaronlineshop.di.scopes.AppScope
import com.abduqodirov.guitaronlineshop.view.ShopApplication
import com.abduqodirov.guitaronlineshop.view.screens.auth.di.component.AuthComponent
import com.abduqodirov.guitaronlineshop.view.screens.productdisplaying.di.component.ProductDisplayingComponent
import com.abduqodirov.guitaronlineshop.view.screens.submitnewproduct.di.component.SubmitComponent
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector

@AppScope
@Component(
    modules = [
        AndroidInjectionModule::class,
        AppModule::class,
        ViewModelFactoryModule::class,
        SubcomponentsModule::class
    ]
)
interface AppComponent : AndroidInjector<ShopApplication> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): AppComponent
    }

    fun submitComponent(): SubmitComponent.Factory

    fun productsDisplayComponent(): ProductDisplayingComponent.Factory

    fun authComponent(): AuthComponent.Factory
}

@Module(subcomponents = [SubmitComponent::class, ProductDisplayingComponent::class, AuthComponent::class])
class SubcomponentsModule
