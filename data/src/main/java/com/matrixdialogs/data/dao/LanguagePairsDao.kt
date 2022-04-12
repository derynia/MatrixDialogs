package com.matrixdialogs.data.dao

import androidx.room.*
import com.matrixdialogs.data.dataclass.LanguageSelected
import com.matrixdialogs.data.entity.LanguagePairs
import kotlinx.coroutines.flow.Flow

@Dao
interface LanguagePairsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(languagePairs: LanguagePairs)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(languagePairsList: List<LanguagePairs>)

    @Query("SELECT * FROM LanguagePairs")
    fun languagePairsList() : List<LanguagePairs>

    @Query("SELECT * FROM LanguagePairs")
    fun getLanguagePairsList() : Flow<List<LanguagePairs>>

    @Transaction
    @Query("SELECT LanguagePairs.source_lang_code, LangSource.code AS code, LangSource.name AS name," +
            " LanguagePairs.dest_lang_code, LangDest.code AS d_code, LangDest.name AS d_name" +
            " FROM LanguagePairs" +
            " INNER JOIN Language AS LangSource ON LanguagePairs.source_lang_code = LangSource.code" +
            " INNER JOIN Language AS LangDest ON LanguagePairs.dest_lang_code = LangDest.code")
    fun getPairsList() : Flow<List<LanguageSelected>>
}