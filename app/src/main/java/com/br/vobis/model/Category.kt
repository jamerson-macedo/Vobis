package com.br.vobis.model

import android.os.Parcel
import android.os.Parcelable

class Category() : IEntity, Parcelable {
    override var id: String? = null
    lateinit var name: String

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        name = parcel.readString()
    }

    constructor(name: String) : this() {
        this.name = name
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Category> {
        override fun createFromParcel(parcel: Parcel): Category {
            return Category(parcel)
        }

        override fun newArray(size: Int): Array<Category?> {
            return arrayOfNulls(size)
        }
    }
}