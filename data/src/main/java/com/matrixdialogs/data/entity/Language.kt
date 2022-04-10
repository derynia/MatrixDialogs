package com.matrixdialogs.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Language(
    @PrimaryKey val item_id: Int = 0,
    @ColumnInfo(name = "name") var name: String
) {
    override fun toString(): String {
        return this.name
    }
}
