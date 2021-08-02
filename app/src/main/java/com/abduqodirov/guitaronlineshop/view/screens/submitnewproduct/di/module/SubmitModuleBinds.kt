package com.abduqodirov.guitaronlineshop.view.screens.submitnewproduct.di.module

import com.abduqodirov.guitaronlineshop.data.repository.submitting.SubmitProductRepository
import com.abduqodirov.guitaronlineshop.data.repository.submitting.SubmitProductRepositoryImpl
import com.abduqodirov.guitaronlineshop.di.scopes.FragmentScope
import dagger.Binds
import dagger.Module

@Module
abstract class SubmitModuleBinds {

    @FragmentScope
    @Binds
    abstract fun bindSubmitRepository(repo: SubmitProductRepositoryImpl): SubmitProductRepository
}
