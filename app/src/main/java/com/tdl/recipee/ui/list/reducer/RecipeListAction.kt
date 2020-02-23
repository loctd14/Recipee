package com.tdl.recipee.ui.list.reducer

import android.graphics.Bitmap
import com.tdl.recipee.data.model.Recipe
import com.tdl.recipee.ui.base.mvi.NavigationAction

/**
 * Created by loc.ta on 2/22/2020.
 */
sealed class RecipeListAction {

    object EmptyAction : RecipeListAction()

    class LoadRecipes(val recipes: List<Pair<Recipe, Bitmap?>>) : RecipeListAction()

    class ReloadRecipes(val recipes: List<Pair<Recipe, Bitmap?>>) : RecipeListAction(), NavigationAction

    class DeleteRecipe(val recipe: Pair<Recipe, Bitmap?>): RecipeListAction(), NavigationAction

    class SearchRecipes(val recipe: List<Pair<Recipe, Bitmap?>>): RecipeListAction(), NavigationAction
}