package com.matrixdialogs.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.matrixdialogs.data.entity.Language
import kotlinx.coroutines.flow.Flow

@Dao
interface LanguageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(language: Language)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(languageList: List<Language>)

    @Query("SELECT * FROM Language")
    fun languagesList() : List<Language>

    @Query("SELECT * FROM Language")
    fun getLanguages() : Flow<List<Language>>
}