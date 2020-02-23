package com.tdl.recipee.data.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by loc.ta on 2/22/2020.
 */
data class Ingredient(val name: String?,
                      val amount: String? = "") : Parcelable {

    constructor(source: Parcel) : this(
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(name)
        writeString(amount)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Ingredient> = object : Parcelable.Creator<Ingredient> {
            override fun createFromParcel(source: Parcel): Ingredient = Ingredient(source)
            override fun newArray(size: Int): Array<Ingredient?> = arrayOfNulls(size)
        }
    }
}
