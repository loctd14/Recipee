package com.tdl.recipee.data.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by loc.ta on 2/22/2020.
 */
data class Step(val name: String?,
                val description: String?) : Parcelable {

    constructor(source: Parcel) : this(
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(name)
        writeString(description)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Step> = object : Parcelable.Creator<Step> {
            override fun createFromParcel(source: Parcel): Step = Step(source)
            override fun newArray(size: Int): Array<Step?> = arrayOfNulls(size)
        }
    }
}
