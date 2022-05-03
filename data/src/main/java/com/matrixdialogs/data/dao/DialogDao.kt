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

    @Query("SELECT * FROM Dialog WHERE language_from_code = :languageFromCode AND language_to_code = :languageToCode")
    fun getDialogsByLanguagePair(languageFromCode: String, languageToCode: String) : Flow<List<Dialog>>

    @Query("DELETE FROM Dialog WHERE item_id = :id")
    fun deleteDialog(id: Int)

    @Query("SELECT * FROM Dialog WHERE item_id = :id")
    fun getById(id: Int) : Dialog
}