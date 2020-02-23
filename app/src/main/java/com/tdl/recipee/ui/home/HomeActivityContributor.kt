package com.tdl.recipee.ui.home

import com.tdl.recipee.ui.update.AddOrEditRecipeFragment
import com.tdl.recipee.ui.update.AddOrEditRecipeModule
import com.tdl.recipee.ui.list.RecipeListFragment
import com.tdl.recipee.ui.list.RecipeListModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by loc.ta on 2/22/2020.
 */
@Module
abstract class HomeActivityContributor {

    @ContributesAndroidInjector(modules = [RecipeListModule::class])
    abstract fun provideRecipeListFragment(): RecipeListFragment

    @ContributesAndroidInjector(modules = [AddOrEditRecipeModule::class])
    abstract fun provideAddRecipeFragment(): AddOrEditRecipeFragment
}