package com.abduqodirov.guitaronlineshop.view

import android.app.Application
import com.abduqodirov.guitaronlineshop.BuildConfig
import com.abduqodirov.guitaronlineshop.di.component.AppComponent
import com.abduqodirov.guitaronlineshop.di.component.DaggerAppComponent
import timber.log.Timber

class ShopApplication : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(this.applicationContext)
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
