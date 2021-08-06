package com.abduqodirov.guitaronlineshop.view.screens.profile.di.modules

import com.abduqodirov.guitaronlineshop.data.repository.auth.AuthRepository
import com.abduqodirov.guitaronlineshop.data.repository.auth.AuthRepositoryImpl
import com.abduqodirov.guitaronlineshop.di.scopes.FragmentScope
import dagger.Binds
import dagger.Module

@Module
abstract class ProfileModuleBinds {

    @FragmentScope
    @Binds
    abstract fun bindAuthRepository(repo: AuthRepositoryImpl): AuthRepository
}
