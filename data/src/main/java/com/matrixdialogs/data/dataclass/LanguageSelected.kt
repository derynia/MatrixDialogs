package com.matrixdialogs.data.dataclass

import androidx.room.Embedded
import androidx.room.Relation
import com.matrixdialogs.data.entity.Language
import com.matrixdialogs.data.entity.LanguagePairs

data class LanguageSelected(
    @Embedded val sourceLanguage: Language,
    @Embedded(prefix = "d_") val destLanguage: Language
)
