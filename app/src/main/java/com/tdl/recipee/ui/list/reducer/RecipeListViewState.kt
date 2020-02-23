package com.tdl.recipee.ui.list.reducer

import android.graphics.Bitmap
import com.tdl.recipee.data.model.Recipe

/**
 * Created by loc.ta on 2/22/2020.
 */
data class RecipeListViewState(val recipes: List<Pair<Recipe, Bitmap?>>? = null) {

    companion object {
        val INITIAL_STATE = RecipeListViewState()
    }
}