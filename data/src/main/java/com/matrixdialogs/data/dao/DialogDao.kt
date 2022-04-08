package com.matrixdialogs.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.matrixdialogs.data.entity.Dialog
import kotlinx.coroutines.flow.Flow

@Dao
interface DialogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dialogItem: Dialog)

    @Query("SELECT * FROM Dialog")
    fun getDialogs() : Flow<List<Dialog>>

    @Query("SELECT * FROM Dialog WHERE languageFrom = :languageFrom AND languageTo = :languageTo")
    fun getDialogsByLanguagePair(languageFrom: Int, languageTo: Int) : Flow<List<Dialog>>

    @Query("DELETE FROM Dialog WHERE item_id = :id")
    fun deleteDialog(id: Int)
}