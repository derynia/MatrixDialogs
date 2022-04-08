package com.matrixdialogs.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Dialog")
data class Dialog(
    @PrimaryKey val item_id: Int = 0,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "fileName") var fileName: String,
    @ColumnInfo(name = "languageFrom") var languageFrom: Int,
    @ColumnInfo(name = "languageTo") var languageTo: Int,
    @ColumnInfo(name = "text") var text: String,
    @ColumnInfo(name = "translation") var translation: String,
    var repeats: Int
)
