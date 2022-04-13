package com.matrixdialogs.data.repository

import com.matrixdialogs.data.MatrixDB
import com.matrixdialogs.data.entity.Language
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LanguageRepository @Inject constructor(
    database: MatrixDB
)  {
    private val languageDao = database.languageDao()

    suspend fun insert(language: Language) {
        languageDao.insert(language)
    }

    fun getLanguages() : Flow<List<Language>> = languageDao.getLanguages()

    fun getEmptyLanguage() : Language = Language("", "", "")
}