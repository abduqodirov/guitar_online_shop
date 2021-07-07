package com.abduqodirov.guitaronlineshop.view.screens.submitnewproduct.di

import com.abduqodirov.guitaronlineshop.data.repository.submitting.SubmitProductRepository
import com.abduqodirov.guitaronlineshop.data.repository.submitting.SubmitProductRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class SubmitModuleBinds {

    @FragmentScope
    @Binds
    abstract fun bindSubmitRepository(repo: SubmitProductRepositoryImpl): SubmitProductRepository
}
