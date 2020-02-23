package com.tdl.recipee.ui.update

import com.tdl.recipee.data.model.Ingredient
import com.tdl.recipee.data.model.Recipe
import com.tdl.recipee.data.model.Step
import com.tdl.recipee.ui.base.mvi.MviView
import com.tdl.recipee.ui.update.reducer.AddOrEditRecipeViewState
import io.reactivex.Observable

/**
 * Created by loc.ta on 2/23/2020.
 */
interface AddOrEditRecipeView: MviView {

    fun render(state: AddOrEditRecipeViewState)

    fun loadImageIntent(): Observable<String>

    fun updateIngredientIntent(): Observable<Pair<Ingredient, Ingredient?>>

    fun updateStepIntent(): Observable<Pair<Step, Step?>>

    fun validateIntent(): Observable<Boolean>

    fun saveIntent(): Observable<Boolean>

    fun updateNameIntent(): Observable<String>

    fun updateCategoryIntent(): Observable<String>

    fun persistIntent(): Observable<Recipe>

    fun removeIngredientIntent(): Observable<Ingredient>

    fun removeStepIntent(): Observable<Step>
}