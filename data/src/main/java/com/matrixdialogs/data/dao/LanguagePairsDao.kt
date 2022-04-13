package com.matrixdialogs.data.dao

import androidx.room.*
import com.matrixdialogs.data.dataclass.LanguageSelected
import com.matrixdialogs.data.entity.LanguagePairs
import kotlinx.coroutines.flow.Flow

const val langSelectedMainQueryText: String =
    "SELECT LanguagePairs.source_lang_code, LangSource.code AS code, LangSource.name AS name," +
        " LanguagePairs.dest_lang_code, LangDest.code AS d_code, LangDest.name AS d_name" +
        " FROM LanguagePairs"

const val langSelectedAllText : String =
        langSelectedMainQueryText +
        " INNER JOIN Language AS LangSource ON LanguagePairs.source_lang_code = LangSource.code" +
        " INNER JOIN Language AS LangDest ON LanguagePairs.dest_lang_code = LangDest.code"

const val langSelectedByCodeText : String =
        langSelectedMainQueryText +
        " INNER JOIN Language AS LangSource ON LanguagePairs.source_lang_code = LangSource.code AND LangSource.code = :sourceCode" +
        " INNER JOIN Language AS LangDest ON LanguagePairs.dest_lang_code = LangDest.code AND LangDest.code = :destCode"

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
    @Query(langSelectedAllText)
    fun getPairsList() : Flow<List<LanguageSelected>>

    @Transaction
    @Query(langSelectedByCodeText)
    fun getPairsList(sourceCode: String, destCode: String) : Flow<List<LanguageSelected>>
}