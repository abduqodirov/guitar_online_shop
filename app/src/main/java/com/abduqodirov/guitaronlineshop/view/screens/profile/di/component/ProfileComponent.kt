package com.abduqodirov.guitaronlineshop.view.screens.profile.di.component

import com.abduqodirov.guitaronlineshop.di.scopes.FragmentScope
import com.abduqodirov.guitaronlineshop.view.screens.profile.ProfileFragment
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [])
interface ProfileComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): ProfileComponent
    }

    fun inject(fragment: ProfileFragment)
}
