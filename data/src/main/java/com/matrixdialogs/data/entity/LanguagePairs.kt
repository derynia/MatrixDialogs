package com.matrixdialogs.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["source_lang_id", "dest_lang_id"])
data class LanguagePairs(
    @ColumnInfo val source_lang_id: Int = 0,
    @ColumnInfo val dest_lang_id: Int = 0
)
