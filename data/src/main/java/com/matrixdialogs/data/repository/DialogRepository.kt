package com.matrixdialogs.data.repository

import com.matrixdialogs.data.MatrixDB
import com.matrixdialogs.data.entity.Dialog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class DialogRepository @Inject constructor(
    database: MatrixDB
)  {
    private val dialogDao = database.dialogDao()

    suspend fun insert(dialog: Dialog) {
        dialogDao.insert(dialog)
    }

    //fun getDialogs() : Flow<List<Dialog>> = dialogDao.getDialogs()
    fun getDialogs() : Flow<List<Dialog>> {
        val list = mutableListOf<Dialog>()

        list.add(Dialog(0, "First dialog", "file1.mp3", 0, 1, "", "", 10))
        list.add(Dialog(0, "Second dialog", "file2.mp3", 0, 1, "", "", 10))
        list.add(Dialog(0, "Third dialog", "file3.mp3", 0, 1, "", "", 10))

        return flowOf(list)
    }
}