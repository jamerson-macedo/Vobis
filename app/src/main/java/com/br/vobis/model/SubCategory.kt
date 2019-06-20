package com.jedev.vobis_admin.models

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.Timestamp

class SubCategory(): Parcelable {
    var name: String? = null
    var createdOn = Timestamp.now()

    constructor(name: String) : this() {
        this.name = name
    }

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()
        createdOn = parcel.readParcelable(Timestamp::class.java.classLoader)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeParcelable(createdOn, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SubCategory> {
        override fun createFromParcel(parcel: Parcel): SubCategory {
            return SubCategory(parcel)
        }

        override fun newArray(size: Int): Array<SubCategory?> {
            return arrayOfNulls(size)
        }
    }
}