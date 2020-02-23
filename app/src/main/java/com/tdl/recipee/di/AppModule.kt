package com.tdl.recipee.di

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tdl.recipee.data.RecipeModule
import com.tdl.recipee.support.AssetManager
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjectionModule

/**
 * Created by loc.ta on 2/22/2020.
 */
@Module(
    includes = [
        AndroidInjectionModule::class,
        ActivityBuilder::class,
        RecipeModule::class
    ]
)
object AppModule {

    @JvmStatic
    @Provides
    @ApplicationScope
    fun provideGson(): Gson = GsonBuilder()
        .setPrettyPrinting()
        .create()

    @JvmStatic
    @Provides
    @ApplicationScope
    fun provideSharePreference(context: Context): SharedPreferences {
        return context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
    }

    @JvmStatic
    @Provides
    @ApplicationScope
    fun provideAssetManager(context: Context): AssetManager {
        return AssetManager(context)
    }
}