package com.abduqodirov.guitaronlineshop.view

import android.app.Application
import com.abduqodirov.guitaronlineshop.di.component.AppComponent
import com.abduqodirov.guitaronlineshop.di.component.DaggerAppComponent

class ShopApplication : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(this.applicationContext)
    }
}
