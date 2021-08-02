package com.abduqodirov.guitaronlineshop.view.screens.submitnewproduct.di.component

import com.abduqodirov.guitaronlineshop.data.repository.submitting.SubmitProductRepository
import com.abduqodirov.guitaronlineshop.di.scopes.FragmentScope
import com.abduqodirov.guitaronlineshop.view.screens.submitnewproduct.SubmitNewProductFragment
import com.abduqodirov.guitaronlineshop.view.screens.submitnewproduct.di.module.SubmitModule
import com.abduqodirov.guitaronlineshop.view.screens.submitnewproduct.di.module.SubmitModuleBinds
import com.abduqodirov.guitaronlineshop.view.screens.submitnewproduct.di.module.SubmitViewModelModule
import dagger.Subcomponent

@FragmentScope
@Subcomponent(
    modules = [SubmitModuleBinds::class, SubmitViewModelModule::class, SubmitModule::class]
)
interface SubmitComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): SubmitComponent
    }

    val submitProductRepository: SubmitProductRepository

    fun inject(fragment: SubmitNewProductFragment)
}
