package com.abduqodirov.guitaronlineshop.view.screens.submitnewproduct.di.component

import com.abduqodirov.guitaronlineshop.data.repository.submitting.SubmitProductRepository
import com.abduqodirov.guitaronlineshop.view.screens.submitnewproduct.SubmitNewProductFragment
import com.abduqodirov.guitaronlineshop.view.screens.submitnewproduct.di.module.SubmitModule
import com.abduqodirov.guitaronlineshop.view.screens.submitnewproduct.di.module.SubmitModuleBinds
import com.abduqodirov.guitaronlineshop.view.screens.submitnewproduct.di.module.SubmitViewModelModule
import dagger.Subcomponent
import javax.inject.Scope

@Scope
@Retention(value = AnnotationRetention.RUNTIME)
annotation class FragmentScope

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
