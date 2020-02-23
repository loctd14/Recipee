package com.tdl.recipee.data.local

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tdl.recipee.data.model.Recipe
import com.tdl.recipee.extension.forceReload
import com.tdl.recipee.extension.serializedRecipes
import io.reactivex.Observable

/**
 * Created by loc.ta on 2/22/2020.
 */
class RecipeStore(
    private val gson: Gson,
    private val context: Context,
    private val sharedPreferences: SharedPreferences
) {

    fun fetchRecipes(): Observable<List<Recipe>> {
        return Observable.fromCallable { loadLocalRecipes() }
            .flatMap {
                if (it.isEmpty()) {
                    Observable.empty()
                } else {
                    Observable.just(it)
                }
            }
    }

    private fun loadLocalRecipes(): List<Recipe> {
        return gson.fromJson(
            sharedPreferences.serializedRecipes,
            object : TypeToken<List<Recipe>>() {}.type
        ) ?: emptyList()
    }

    fun persistRecipes(recipes: List<Recipe>) {
        sharedPreferences.serializedRecipes = gson.toJson(recipes)
    }

    fun deleteRecipe(recipe: Recipe) {
        persistRecipes(
            loadLocalRecipes()
                .asSequence()
                .filterNot { it.recipeId == recipe.recipeId }
                .toList()
        )
    }

    fun addRecipe(recipe: Recipe) {
        persistRecipes(
            loadLocalRecipes()
                .toMutableList()
                .apply { add(recipe) }
        )
        sharedPreferences.forceReload = true
    }

    fun editRecipe(recipe: Recipe) {
        val localRecipes = loadLocalRecipes()
        val index = localRecipes.indexOf(localRecipes.find { it.recipeId == recipe.recipeId })
        persistRecipes(
            localRecipes
                .toMutableList()
                .apply { set(index, recipe) }
        )
        sharedPreferences.forceReload = true
    }
}
