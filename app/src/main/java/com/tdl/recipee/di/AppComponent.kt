package com.tdl.recipee.di

import android.content.Context
import com.tdl.recipee.RecipeeApp
import dagger.BindsInstance
import dagger.Component

/**
 * Created by loc.ta on 2/22/2020.
 */
@ApplicationScope
@Component(
    modules = [
        AppModule::class,
        NetworkModule::class
    ]
)
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(context: Context): Builder

        fun build(): AppComponent
    }

    fun inject(app: RecipeeApp)
}
