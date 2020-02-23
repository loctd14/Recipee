package com.tdl.recipee.ui.update

import com.tdl.recipee.data.local.RecipeStore
import com.tdl.recipee.support.ImageService
import com.tdl.recipee.ui.update.reducer.AddOrEditRecipeReducer
import dagger.Module
import dagger.Provides

/**
 * Created by loc.ta on 2/23/2020.
 */
@Module
class AddOrEditRecipeModule {

    @Provides
    fun provideAddOrEditRecipeReducer(): AddOrEditRecipeReducer {
        return AddOrEditRecipeReducer()
    }

    @Provides
    fun provideAddOrEditRecipeInteractor(imageService: ImageService, recipeStore: RecipeStore): AddOrEditRecipeInteractor {
        return AddOrEditRecipeInteractor(imageService, recipeStore)
    }
}