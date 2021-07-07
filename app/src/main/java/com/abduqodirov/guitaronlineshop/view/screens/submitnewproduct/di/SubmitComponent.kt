package com.abduqodirov.guitaronlineshop.view.screens.submitnewproduct.di

import com.abduqodirov.guitaronlineshop.data.repository.submitting.SubmitProductRepository
import com.abduqodirov.guitaronlineshop.view.screens.submitnewproduct.SubmitNewProductFragment
import dagger.Subcomponent
import javax.inject.Scope

@Scope
@Retention(value = AnnotationRetention.RUNTIME)
annotation class FragmentScope

@FragmentScope
@Subcomponent(modules = [SubmitModuleBinds::class, SubmitViewModelModule::class])
interface SubmitComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): SubmitComponent
    }

    fun inject(fragment: SubmitNewProductFragment)

    val submitProductRepository: SubmitProductRepository
}
