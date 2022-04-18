package com.matrixdialogs.data.repository

import com.matrixdialogs.data.MatrixDB
import com.matrixdialogs.data.dataclass.LanguageSelected
import com.matrixdialogs.data.entity.LanguagePairs
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LanguageSelectedRepository @Inject constructor(
    database: MatrixDB
)  {
    private val languagePairsDao = database.languagePairsDao()

    suspend fun insert(languagePairs: LanguagePairs) = languagePairsDao.insert(languagePairs)

    fun getLanguageSelectedList() : Flow<List<LanguageSelected>> = languagePairsDao.getPairsList()
}
