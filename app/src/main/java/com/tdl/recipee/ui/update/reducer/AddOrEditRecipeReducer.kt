package com.tdl.recipee.ui.update.reducer

import com.tdl.recipee.ui.base.redux.Store
import com.tdl.recipee.ui.update.reducer.AddOrEditRecipeAction.*
import com.tdl.recipee.ui.update.reducer.AddOrEditRecipeViewState.AddRecipeViewState
import com.tdl.recipee.ui.update.reducer.AddOrEditRecipeViewState.EditRecipeViewState

/**
 * Created by loc.ta on 2/23/2020.
 */
class AddOrEditRecipeReducer : Store.Reducer<AddOrEditRecipeAction, AddOrEditRecipeViewState> {

    override fun reduce(
        action: AddOrEditRecipeAction,
        currentState: AddOrEditRecipeViewState
    ): AddOrEditRecipeViewState {
        return when (action) {
            is LoadImage -> currentState.parentCopy(
                recipe = currentState.recipe?.copy(imageUrl = action.url)
            )
            is AddNewIngredient -> currentState.parentCopy(
                recipe = currentState.recipe?.copy(
                    ingredients = currentState.recipe?.ingredients?.toMutableList()?.apply {
                        add(action.ingredient)
                    })
            )
            is EditIngredient -> currentState.parentCopy(
                recipe = currentState.recipe?.copy(
                    ingredients = currentState.recipe?.ingredients?.toMutableList()?.apply {
                        set(indexOf(action.oldIngredient), action.ingredient)
                    })
            )
            is AddNewStep -> currentState.parentCopy(
                recipe = currentState.recipe?.copy(
                    steps = currentState.recipe?.steps?.toMutableList()?.apply {
                        add(action.step)
                    })
            )
            is EditStep -> currentState.parentCopy(
                recipe = currentState.recipe?.copy(
                    steps = currentState.recipe?.steps?.toMutableList()?.apply {
                        set(indexOf(action.oldStep), action.step)
                    })
            )
            is Validate -> currentState.parentCopy(isValid = action.isValid)
            is UpdateName -> currentState.parentCopy(recipe = currentState.recipe?.copy(name = action.name))
            is UpdateCategory -> currentState.parentCopy(recipe = currentState.recipe?.copy(category = action.category))
            is Persist -> when (currentState) {
                is AddRecipeViewState -> currentState
                is EditRecipeViewState -> if (currentState.recipe == null) currentState.copy(recipe = action.recipe) else currentState
            }
            is RemoveIngredient -> currentState.parentCopy(
                recipe = currentState.recipe?.copy(
                    ingredients = currentState.recipe?.ingredients.orEmpty().toMutableList().apply {
                        remove(action.ingredient)
                    }
                )
            )
            is RemoveStep -> currentState.parentCopy(
                recipe = currentState.recipe?.copy(
                    steps = currentState.recipe?.steps.orEmpty().toMutableList().apply {
                        remove(action.step)
                    }
                )
            )
            SaveRecipe -> currentState
        }
    }
}