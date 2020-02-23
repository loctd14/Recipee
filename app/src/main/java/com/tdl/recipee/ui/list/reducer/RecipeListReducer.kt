package com.tdl.recipee.ui.list.reducer

import com.tdl.recipee.ui.base.redux.Store
import com.tdl.recipee.ui.list.reducer.RecipeListAction.*

/**
 * Created by loc.ta on 2/22/2020.
 */
class RecipeListReducer : Store.Reducer<RecipeListAction, RecipeListViewState> {

    override fun reduce(
        action: RecipeListAction,
        currentState: RecipeListViewState
    ): RecipeListViewState {
        return when (action) {
            is LoadRecipes -> if (currentState.recipes == null) currentState.copy(
                recipes = action.recipes
            ) else currentState
            is DeleteRecipe -> currentState.copy(
                recipes = currentState.recipes?.toMutableList()?.apply {
                    remove(action.recipe)
                }
            )
            is ReloadRecipes -> currentState.copy(recipes = action.recipes)
            is SearchRecipes,  EmptyAction -> currentState
        }
    }
}