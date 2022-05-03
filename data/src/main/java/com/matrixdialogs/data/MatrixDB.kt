package com.matrixdialogs.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.matrixdialogs.core.LANGUAGE_JSON_READER_ERROR
import com.matrixdialogs.data.dao.DialogDao
import com.matrixdialogs.data.dao.LanguageDao
import com.matrixdialogs.data.dao.LanguagePairsDao
import com.matrixdialogs.data.entity.Dialog
import com.matrixdialogs.data.entity.Language
import com.matrixdialogs.data.entity.LanguagePairs
import kotlinx.coroutines.*
import java.io.IOException

@Database(entities = [Dialog::class, Language::class, LanguagePairs::class], version = 6)
abstract class MatrixDB : RoomDatabase() {
    internal abstract fun dialogDao(): DialogDao
    internal abstract fun languageDao(): LanguageDao
    internal abstract fun languagePairsDao(): LanguagePairsDao

    suspend fun populateLanguages(context: Context) = withContext(Dispatchers.IO) {
        val lDao = languageDao()

        if (lDao.languagesList().isNotEmpty()) {
            return@withContext
        }

        lateinit var jsonString: String
        try {
            jsonString = context.assets.open("languages.json")
                .bufferedReader()
                .use { it.readText() }
        } catch (ioException: IOException) {
            Log.d(LANGUAGE_JSON_READER_ERROR, ioException.message.toString())
            return@withContext
        }

        val listCountryType = object : TypeToken<List<Language>>() {}.type
        val langList : ArrayList<Language> = Gson().fromJson(jsonString, listCountryType)

        lDao.insertList(langList)
    }

    companion object {
        fun getInstance(context: Context): MatrixDB {
            val instance = Room.databaseBuilder(context, MatrixDB::class.java, "Matrix Dialogues DB")
                .fallbackToDestructiveMigration()
                .build()

            runBlocking {
                instance.populateLanguages(context)
            }

            return instance
        }
    }
}