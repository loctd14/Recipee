package com.tdl.recipee.ui.list

import android.graphics.Bitmap
import com.tdl.recipee.data.model.Recipe
import com.tdl.recipee.ui.base.mvi.MviView
import com.tdl.recipee.ui.list.reducer.RecipeListViewState
import io.reactivex.Observable

/**
 * Created by loc.ta on 2/22/2020.
 */
interface RecipeListView : MviView {

    fun render(state: RecipeListViewState)

    fun loadInitialIntent(): Observable<Boolean>

    fun deleteRecipeIntent(): Observable<Pair<Recipe, Bitmap?>>

    fun reloadIntent(): Observable<Boolean>

    fun searchIntent(): Observable<String>
}