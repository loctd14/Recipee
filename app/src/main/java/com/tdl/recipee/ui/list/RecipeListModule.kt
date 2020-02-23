package com.tdl.recipee.ui.list

import com.tdl.recipee.data.local.RecipeStore
import com.tdl.recipee.support.AssetManager
import com.tdl.recipee.support.ImageService
import com.tdl.recipee.ui.list.reducer.RecipeListReducer
import dagger.Module
import dagger.Provides

/**
 * Created by loc.ta on 2/22/2020.
 */
@Module
class RecipeListModule {

    @Provides
    fun provideRecipeListPresenter(
        interactor: RecipeListInteractor,
        reducer: RecipeListReducer
    ): RecipeListPresenter {
        return RecipeListPresenter(interactor, reducer)
    }

    @Provides
    fun provideRecipeListInteractor(
        recipeStore: RecipeStore,
        assetManager: AssetManager,
        imageService: ImageService
    ): RecipeListInteractor {
        return RecipeListInteractor(recipeStore, assetManager, imageService)
    }

    @Provides
    fun provideRecipeListReducer(): RecipeListReducer {
        return RecipeListReducer()
    }
}