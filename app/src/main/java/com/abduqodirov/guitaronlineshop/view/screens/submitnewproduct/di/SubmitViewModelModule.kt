package com.abduqodirov.guitaronlineshop.view.screens.submitnewproduct.di

import androidx.lifecycle.ViewModel
import com.abduqodirov.guitaronlineshop.di.module.ViewModelKey
import com.abduqodirov.guitaronlineshop.view.screens.submitnewproduct.SubmitProductViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SubmitViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SubmitProductViewModel::class)
    abstract fun bindsSubmitProductViewModel(
        submitProductViewModel: SubmitProductViewModel
    ): ViewModel
}
