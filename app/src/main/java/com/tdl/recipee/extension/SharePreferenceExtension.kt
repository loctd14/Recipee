package com.tdl.recipee.extension

import android.content.SharedPreferences

/**
 * Created by loc.ta on 2/22/2020.
 */
private const val FORCE_RELOAD = "rc_force_reload"

private const val SERIALIZED_RECIPES = "rc_serialized_recipes"

inline fun SharedPreferences.editor(block: (SharedPreferences.Editor) -> Unit) {
    val editor = edit()
    block(editor)
    editor.apply()
}

fun SharedPreferences.clearValues() = editor { it.clear() }

var SharedPreferences.forceReload: Boolean
    get() = getBoolean(FORCE_RELOAD, false)
    set(value) {
        editor {
            it.putBoolean(FORCE_RELOAD, value)
        }
    }

var SharedPreferences.serializedRecipes: String
    get() = getString(SERIALIZED_RECIPES, "").orEmpty()
    set(value) {
        editor {
            it.putString(SERIALIZED_RECIPES, value)
        }
    }