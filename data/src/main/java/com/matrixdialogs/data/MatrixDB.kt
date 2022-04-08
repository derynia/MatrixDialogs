package com.matrixdialogs.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.matrixdialogs.data.dao.DialogDao
import com.matrixdialogs.data.dao.LanguageDao
import com.matrixdialogs.data.entity.Dialog
import com.matrixdialogs.data.entity.Language
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Database(entities = [Dialog::class, Language::class], version = 1)
abstract class MatrixDB : RoomDatabase() {
    internal abstract fun dialogDao(): DialogDao
    internal abstract fun languageDao(): LanguageDao

    fun populateLanguages(context: Context) {
        val lDao = languageDao()

        GlobalScope.launch {
            if (lDao.languagesList().isEmpty()) {
                val langList : ArrayList<Language> = ArrayList()
                val sourceList = context.resources.getStringArray(R.array.lang_array)
                for(i in sourceList.indices) {
                    langList.add(Language(i, sourceList[i]))
                }

                lDao.insertList(langList)
            }
        }
    }

    companion object {
        fun getInstance(context: Context): MatrixDB {
            val instance = Room.databaseBuilder(context, MatrixDB::class.java, "Matrix Dialogues DB")
                .fallbackToDestructiveMigration()
                .build()

            instance.populateLanguages(context)
            return instance
        }
    }
}