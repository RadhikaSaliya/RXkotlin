package com.user.canopas.rxkotlin

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class Actorwapper(@SerializedName("actors") internal val result: List<Actors>)

data class Actors(
        internal var name: String,
        internal var description: String,
        internal var dob: String,
        internal var country: String,
        internal var height: String,
        internal var spouse: String,
        internal var children: String,
        internal var image: String) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(dob)
        parcel.writeString(country)
        parcel.writeString(height)
        parcel.writeString(spouse)
        parcel.writeString(children)
        parcel.writeString(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Actors> {
        override fun createFromParcel(parcel: Parcel): Actors {
            return Actors(parcel)
        }

        override fun newArray(size: Int): Array<Actors?> {
            return arrayOfNulls(size)
        }
    }
}





