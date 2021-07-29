package com.abduqodirov.guitaronlineshop.view.screens.auth.di.module

import com.abduqodirov.guitaronlineshop.data.repository.auth.AuthRepository
import com.abduqodirov.guitaronlineshop.data.repository.auth.AuthRepositoryImpl
import com.abduqodirov.guitaronlineshop.view.screens.submitnewproduct.di.component.FragmentScope
import dagger.Binds
import dagger.Module

@Module
abstract class AuthModuleBinds {

    @FragmentScope
    @Binds
    abstract fun bindAuthRepository(repo: AuthRepositoryImpl): AuthRepository
}
