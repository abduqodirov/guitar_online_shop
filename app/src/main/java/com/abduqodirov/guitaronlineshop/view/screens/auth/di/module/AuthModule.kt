package com.abduqodirov.guitaronlineshop.view.screens.auth.di.module

import androidx.lifecycle.ViewModel
import com.abduqodirov.guitaronlineshop.di.module.ViewModelKey
import com.abduqodirov.guitaronlineshop.view.screens.auth.AuthViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AuthModule {

    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    abstract fun bindsAuthViewModel(authViewModel: AuthViewModel): ViewModel
}
