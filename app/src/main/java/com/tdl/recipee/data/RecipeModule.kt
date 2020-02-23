package com.tdl.recipee.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.tdl.recipee.data.local.RecipeStore
import com.tdl.recipee.di.ApplicationScope
import dagger.Module
import dagger.Provides

/**
 * Created by loc.ta on 2/22/2020.
 */
@Module
object RecipeModule {

    @JvmStatic
    @Provides
    @ApplicationScope
    fun provideRecipeStore(gson: Gson,
                           context: Context,
                           sharedPreferences: SharedPreferences) = RecipeStore(
            gson,
            context,
            sharedPreferences
    )
}