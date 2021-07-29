package com.abduqodirov.guitaronlineshop.view.screens.auth.di.component

import com.abduqodirov.guitaronlineshop.view.screens.auth.di.module.AuthModule
import com.abduqodirov.guitaronlineshop.view.screens.auth.di.module.AuthModuleBinds
import com.abduqodirov.guitaronlineshop.view.screens.auth.email.EmailSignInFragment
import com.abduqodirov.guitaronlineshop.view.screens.auth.forget.ForgetPasswordFragment
import com.abduqodirov.guitaronlineshop.view.screens.auth.signup.SignUpFragment
import com.abduqodirov.guitaronlineshop.view.screens.submitnewproduct.di.component.FragmentScope
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [AuthModule::class, AuthModuleBinds::class])
interface AuthComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): AuthComponent
    }

    fun inject(fragment: EmailSignInFragment)

    fun inject(fragment: ForgetPasswordFragment)

    fun inject(fragment: SignUpFragment)
}
