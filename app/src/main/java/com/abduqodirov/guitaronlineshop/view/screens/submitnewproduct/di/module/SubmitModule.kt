package com.abduqodirov.guitaronlineshop.view.screens.submitnewproduct.di.module

import com.abduqodirov.guitaronlineshop.data.network.imageuploader.FirebaseImageUploader
import com.abduqodirov.guitaronlineshop.data.network.imageuploader.ImageUploader
import com.abduqodirov.guitaronlineshop.di.scopes.FragmentScope
import dagger.Module
import dagger.Provides

@Module
class SubmitModule {

    @FragmentScope
    @Provides
    fun provideImageUploader(): ImageUploader {
        return FirebaseImageUploader()
    }
}
