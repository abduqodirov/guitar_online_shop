package com.abduqodirov.guitaronlineshop.view.screens.profile.di.modules

import androidx.lifecycle.ViewModel
import com.abduqodirov.guitaronlineshop.di.module.ViewModelKey
import com.abduqodirov.guitaronlineshop.view.screens.profile.ProfileViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ProfileModule {

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    abstract fun bindsProfileViewModel(profileViewModel: ProfileViewModel): ViewModel
}
