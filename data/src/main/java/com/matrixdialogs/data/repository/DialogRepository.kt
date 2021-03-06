package com.matrixdialogs.data.repository

import com.matrixdialogs.data.MatrixDB
import com.matrixdialogs.data.dataclass.LanguageSelected
import com.matrixdialogs.data.entity.Dialog
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DialogRepository @Inject constructor(
    database: MatrixDB
)  {
    private val dialogDao = database.dialogDao()

    suspend fun insert(dialog: Dialog) {
        dialogDao.insert(dialog)
    }

    fun getDialogsByPair(languageSelected: LanguageSelected) : Flow<List<Dialog>>
        = dialogDao.getDialogsByLanguagePair(languageSelected.sourceLanguage?.code ?: "",
            languageSelected.destLanguage?.code ?: "")

    fun getById(id: Int) : Dialog = dialogDao.getById(id)
    fun getDialogs() : Flow<List<Dialog>> = dialogDao.getDialogs()
}