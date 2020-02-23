package com.tdl.recipee.ui.update.reducer

import com.tdl.recipee.data.model.Recipe

/**
 * Created by loc.ta on 2/23/2020.
 */
sealed class AddOrEditRecipeViewState {

    abstract val recipe: Recipe?

    open val isValid: Boolean = false

    abstract fun parentCopy(recipe: Recipe? = null,
                            isValid: Boolean? = null): AddOrEditRecipeViewState

    data class AddRecipeViewState(override val recipe: Recipe? = Recipe(),
                                  override val isValid: Boolean = false) :
        AddOrEditRecipeViewState() {

        override fun parentCopy(recipe: Recipe?, isValid: Boolean?): AddOrEditRecipeViewState =
            copy(recipe = recipe ?: this.recipe, isValid = isValid ?: this.isValid)
    }

    data class EditRecipeViewState(override val recipe: Recipe? = null,
                                   override val isValid: Boolean = true) :
        AddOrEditRecipeViewState() {

        override fun parentCopy(recipe: Recipe?, isValid: Boolean?): AddOrEditRecipeViewState =
            copy(recipe = recipe ?: this.recipe, isValid = isValid ?: this.isValid)
    }

    companion object {
        operator fun invoke(isAddNewRecipe: Boolean): AddOrEditRecipeViewState {
            return when (isAddNewRecipe) {
                true -> AddRecipeViewState()
                false -> EditRecipeViewState()
            }
        }
    }
}