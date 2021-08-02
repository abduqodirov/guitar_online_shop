package com.abduqodirov.guitaronlineshop.di.scopes

import javax.inject.Scope

@Scope
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class AppScope

@Scope
@Retention(value = AnnotationRetention.RUNTIME)
annotation class FragmentScope
