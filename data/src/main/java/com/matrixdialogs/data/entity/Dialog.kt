package com.matrixdialogs.data.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Dialog")
data class Dialog(
    @PrimaryKey(autoGenerate = true) val item_id: Int = 0,
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "fileName") var fileName: String = "",
    @ColumnInfo(name = "language_from_code") var languageFromCode: String = "",
    @Embedded(prefix = "f_") var languageFrom: Language? = null,
    @ColumnInfo(name = "language_to_code") var languageToCode: String = "",
    @Embedded(prefix = "d_") var languageTo: Language? = null,
    @ColumnInfo(name = "text") var text: String = "",
    @ColumnInfo(name = "translation") var translation: String = "",
    var repeats: Int = 0
)
