package com.tdl.recipee.data.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by loc.ta on 2/22/2020.
 */
data class Recipe(
    val recipeId: String? = null,
    val name: String? = null,
    val category: String? = "",
    val ingredients: List<Ingredient>? = emptyList(),
    val steps: List<Step>? = emptyList(),
    val imageUrl: String? = ""
) : Parcelable {

    constructor(source: Parcel) : this(
        source.readString(),
        source.readString(),
        source.readString(),
        source.createTypedArrayList(Ingredient.CREATOR),
        source.createTypedArrayList(Step.CREATOR),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(recipeId)
        writeString(name)
        writeString(category)
        writeTypedList(ingredients)
        writeTypedList(steps)
        writeString(imageUrl)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Recipe> = object : Parcelable.Creator<Recipe> {
            override fun createFromParcel(source: Parcel): Recipe = Recipe(source)
            override fun newArray(size: Int): Array<Recipe?> = arrayOfNulls(size)
        }
    }
}
