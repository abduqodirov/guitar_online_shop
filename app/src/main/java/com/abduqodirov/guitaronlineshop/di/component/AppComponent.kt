package com.abduqodirov.guitaronlineshop.di.component

import android.content.Context
import com.abduqodirov.guitaronlineshop.di.module.AppModule
import com.abduqodirov.guitaronlineshop.di.module.ViewModelFactoryModule
import com.abduqodirov.guitaronlineshop.view.screens.auth.di.component.AuthComponent
import com.abduqodirov.guitaronlineshop.view.screens.productdisplaying.di.component.ProductDisplayingComponent
import com.abduqodirov.guitaronlineshop.view.screens.submitnewproduct.di.component.SubmitComponent
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        ViewModelFactoryModule::class,
        SubcomponentsModule::class
    ]
)
interface AppComponent {

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
