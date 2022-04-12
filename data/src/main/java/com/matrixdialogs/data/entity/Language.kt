package com.matrixdialogs.data.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Language(
    @PrimaryKey @NonNull @ColumnInfo(name = "code") val code: String,
    @ColumnInfo(name = "name") var name: String?,
    @ColumnInfo(name = "nativeName") val nativeName: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun toString() = "${this.name} (${this.nativeName})"

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(code)
        parcel.writeString(name)
        parcel.writeString(nativeName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Language> {
        override fun createFromParcel(parcel: Parcel): Language {
            return Language(parcel)
        }

        override fun newArray(size: Int): Array<Language?> {
            return arrayOfNulls(size)
        }
    }
}
