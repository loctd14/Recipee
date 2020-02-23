package com.tdl.recipee

import android.app.Application
import com.tdl.recipee.di.AppComponent
import com.tdl.recipee.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

/**
 * Created by loc.ta on 2/22/2020.
 */
class RecipeeApp : Application(), HasAndroidInjector {

    private lateinit var component: AppComponent

    @Inject
    lateinit var dispatchingInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = dispatchingInjector

    override fun onCreate() {
        super.onCreate()

        component = DaggerAppComponent.builder()
                .application(this)
                .build()
        component.inject(this)
    }
}