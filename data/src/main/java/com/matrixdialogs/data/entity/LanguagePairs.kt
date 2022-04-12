package com.matrixdialogs.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["source_lang_code", "dest_lang_code"])
data class LanguagePairs(
    @ColumnInfo val source_lang_code: String,
    @ColumnInfo val dest_lang_code: String
)
