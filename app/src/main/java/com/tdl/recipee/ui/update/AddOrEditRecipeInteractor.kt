package com.tdl.recipee.ui.update

import com.tdl.recipee.data.local.RecipeStore
import com.tdl.recipee.data.model.Recipe
import com.tdl.recipee.support.ImageService
import com.tdl.recipee.ui.update.reducer.AddOrEditRecipeAction
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by loc.ta on 2/23/2020.
 */
class AddOrEditRecipeInteractor(
    private val imageService: ImageService,
    private val recipeStore: RecipeStore
) {

    fun loadImage(url: String): Observable<AddOrEditRecipeAction> {
        return imageService.loadImage(url)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map<AddOrEditRecipeAction> { AddOrEditRecipeAction.LoadImage(url, it) }
            .onErrorReturn { AddOrEditRecipeAction.LoadImage(url, null) }
    }

    fun addRecipe(recipe: Recipe): Observable<AddOrEditRecipeAction> {
        return Observable.fromCallable {
            recipeStore.addRecipe(recipe.copy(recipeId = "RC${recipe.category}${System.currentTimeMillis()}"))
        }
            .observeOn(AndroidSchedulers.mainThread())
            .map { AddOrEditRecipeAction.SaveRecipe }
    }

    fun editRecipe(recipe: Recipe): Observable<AddOrEditRecipeAction> {
        return Observable.fromCallable {
            recipeStore.editRecipe(recipe)
        }
            .observeOn(AndroidSchedulers.mainThread())
            .map { AddOrEditRecipeAction.SaveRecipe }
    }
}