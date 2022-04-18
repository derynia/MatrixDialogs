package com.matrixdialogs.data.dataclass

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Embedded
import com.matrixdialogs.data.entity.Language

data class LanguageSelected(
    @Embedded val sourceLanguage: Language?,
    @Embedded(prefix = "d_") val destLanguage: Language?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Language::class.java.classLoader),
        parcel.readParcelable(Language::class.java.classLoader)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(sourceLanguage, flags)
        parcel.writeParcelable(destLanguage, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString() = "${sourceLanguage?.name} -> ${destLanguage?.name}"

    companion object CREATOR : Parcelable.Creator<LanguageSelected> {
        override fun createFromParcel(parcel: Parcel): LanguageSelected {
            return LanguageSelected(parcel)
        }

        override fun newArray(size: Int): Array<LanguageSelected?> {
            return arrayOfNulls(size)
        }
    }
}