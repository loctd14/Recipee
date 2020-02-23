package com.tdl.recipee.ui.update.reducer

import android.graphics.Bitmap
import com.tdl.recipee.data.model.Ingredient
import com.tdl.recipee.data.model.Recipe
import com.tdl.recipee.data.model.Step
import com.tdl.recipee.ui.base.mvi.NavigationAction

/**
 * Created by loc.ta on 2/23/2020.
 */
sealed class AddOrEditRecipeAction {

    class LoadImage(val url: String, val image: Bitmap? = null) : AddOrEditRecipeAction(),
        NavigationAction

    class AddNewIngredient(val ingredient: Ingredient) : AddOrEditRecipeAction(), NavigationAction

    class EditIngredient(val ingredient: Ingredient, val oldIngredient: Ingredient) :
        AddOrEditRecipeAction(), NavigationAction

    class AddNewStep(val step: Step) : AddOrEditRecipeAction(), NavigationAction

    class EditStep(val step: Step, val oldStep: Step) : AddOrEditRecipeAction(), NavigationAction

    class Validate(val isValid: Boolean) : AddOrEditRecipeAction()

    class UpdateName(val name: String) : AddOrEditRecipeAction()

    class UpdateCategory(val category: String) : AddOrEditRecipeAction()

    class Persist(val recipe: Recipe) : AddOrEditRecipeAction()

    object SaveRecipe : AddOrEditRecipeAction(), NavigationAction

    class RemoveIngredient(val ingredient: Ingredient) : AddOrEditRecipeAction(), NavigationAction

    class RemoveStep(val step: Step) : AddOrEditRecipeAction(), NavigationAction
}
