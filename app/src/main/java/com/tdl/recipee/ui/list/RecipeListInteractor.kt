package com.tdl.recipee.ui.list

import android.graphics.Bitmap
import com.tdl.recipee.data.local.RecipeStore
import com.tdl.recipee.data.model.Recipe
import com.tdl.recipee.support.AssetManager
import com.tdl.recipee.support.ImageService
import com.tdl.recipee.ui.list.reducer.RecipeListAction
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Created by loc.ta on 2/22/2020.
 */
class RecipeListInteractor(
    private val recipeStore: RecipeStore,
    private val assetManager: AssetManager,
    private val imageService: ImageService
) {

    fun loadRecipes(): Observable<RecipeListAction> {
        return recipeStore.fetchRecipes()
            .switchIfEmpty(assetManager.loadFromAsset())
            .switchMapSingle { recipes ->
                Observable.fromIterable(recipes)
                    .flatMap { recipe ->
                        imageService.loadImage(recipe.imageUrl.toString())
                            .map { bitmap -> recipe to bitmap }
                            .onErrorReturn { null }
                    }
                    .toList()
                    .doOnSuccess { recipeStore.persistRecipes(recipes) }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map<RecipeListAction> { RecipeListAction.LoadRecipes(it) }
            .onErrorResumeNext(Observable.just(RecipeListAction.EmptyAction))
    }

    fun deleteRecipe(recipe: Pair<Recipe, Bitmap?>): Observable<RecipeListAction> {
        return Observable.fromCallable { recipeStore.deleteRecipe(recipe.first) }
            .map<RecipeListAction> { RecipeListAction.DeleteRecipe(recipe) }
    }

    fun reloadRecipes(): Observable<RecipeListAction> {
        return recipeStore.fetchRecipes()
            .switchMapSingle { recipes ->
                Observable.fromIterable(recipes)
                    .flatMap { recipe ->
                        imageService.loadImage(recipe.imageUrl.toString())
                            .map { bitmap -> recipe to bitmap }
                            .onErrorReturn { null }
                    }
                    .toList()
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map<RecipeListAction> { RecipeListAction.ReloadRecipes(it) }
            .onErrorResumeNext(Observable.just(RecipeListAction.EmptyAction))
    }

    fun searchRecipe(
        keyword: String,
        recipes: List<Pair<Recipe, Bitmap?>>
    ): Observable<RecipeListAction> {
        return Observable.just(keyword)
            .flatMapSingle {
                if (it.isEmpty()) {
                    Single.just(recipes)
                } else {
                    Observable.fromIterable(CopyOnWriteArrayList(recipes))
                        .filter { (recipe, _) ->
                            recipe.category
                                ?.toLowerCase(Locale.getDefault())
                                ?.contains(keyword) == true
                        }
                        .toList()
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .map<RecipeListAction> { RecipeListAction.SearchRecipes(it) }
            .onErrorResumeNext(Observable.just(RecipeListAction.EmptyAction))
    }
}