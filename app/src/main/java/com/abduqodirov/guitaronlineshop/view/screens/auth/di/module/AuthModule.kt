package com.abduqodirov.guitaronlineshop.view.screens.auth.di.module

import androidx.lifecycle.ViewModel
import com.abduqodirov.guitaronlineshop.di.module.ViewModelKey
import com.abduqodirov.guitaronlineshop.view.screens.auth.email.SignInViewModel
import com.abduqodirov.guitaronlineshop.view.screens.auth.signup.SignUpViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AuthModule {

    @Binds
    @IntoMap
    @ViewModelKey(SignInViewModel::class)
    abstract fun bindsSignInViewModel(signInViewModel: SignInViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SignUpViewModel::class)
    abstract fun bindsSignUpViewModel(signUpViewModel: SignUpViewModel): ViewModel
}
